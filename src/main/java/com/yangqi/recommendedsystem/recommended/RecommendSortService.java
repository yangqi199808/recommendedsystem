package com.yangqi.recommendedsystem.recommended;

import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.SparkSession;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Yankee
 * @date 2020/3/11 10:18
 */
@Service
public class RecommendSortService {
    private SparkSession spark;

    private LogisticRegressionModel logisticRegressionModel;

    @PostConstruct
    public void init() {
        // 初始化spark运行环境
        spark = SparkSession.builder().master("local").appName("recommended").getOrCreate();

        // 加载 LR 模型
        logisticRegressionModel = LogisticRegressionModel.load("file:///E:/code/JavaEE/recommendedsystem/src/main/resources/lrmode");
    }

    public List<Integer> sort(List<Integer> shopIdList, Integer userId) {
        // 需要根据 lrmode 所需要的 11 维的 x，生成特征，然后调用其预测方法
        List<ShopSortModel> shopSortModels = new ArrayList<>();

        for (Integer shopId : shopIdList) {
            // 造的假数据，可以从数据库或者缓存中拿到对应的性别，年龄，评分，价格等做特征转换生成 feature 向量
            Vector v = Vectors.dense(1, 0, 0, 0, 0, 1, 0.6, 0, 0, 1, 0);
            Vector result = logisticRegressionModel.predictProbability(v);
            // 只返回 0 或者 1
            // logisticRegressionModel.predict(v);
            double[] arr = result.toArray();
            double score = arr[1];
            ShopSortModel shopSortModel = new ShopSortModel();
            shopSortModel.setShopId(shopId);
            shopSortModel.setScore(score);
            shopSortModels.add(shopSortModel);
        }
        shopSortModels.sort(new Comparator<ShopSortModel>() {
            @Override
            public int compare(ShopSortModel o1, ShopSortModel o2) {
                if (o1.getScore() < o2.getScore()) {
                    return 1;
                } else if (o1.getScore() > o2.getScore()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return shopSortModels.stream().map(ShopSortModel::getShopId).collect(Collectors.toList());
    }
}
