package com.yangqi.recommendedsystem.recommended;

import com.yangqi.recommendedsystem.dal.RecommendDOMapper;
import com.yangqi.recommendedsystem.model.RecommendDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yankee
 * @date 2020/3/11 9:56
 */
@Service
public class RecommendService {
    @Autowired
    private RecommendDOMapper recommendDOMapper;

    /**
     * 召回数据，根据 userId 召回 shopIdList
     *
     * @param userId userId
     * @return 推荐结果
     */
    public List<Integer> recall(Integer userId) {
        RecommendDO recommendDO = recommendDOMapper.selectByPrimaryKey(userId);
        if (recommendDO == null) {
            recommendDO = recommendDOMapper.selectByPrimaryKey(99999999);
        }
        String[] shopIdArr = recommendDO.getRecommend().split(",");
        List<Integer> shopIdList = new ArrayList<>();
        for (int i = 0; i < shopIdArr.length; i++) {
            shopIdList.add(Integer.valueOf(shopIdArr[i]));
        }
        return shopIdList;
    }
}
