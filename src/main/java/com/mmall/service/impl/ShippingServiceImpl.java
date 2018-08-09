package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Luyue
 * @date 2018/8/3 13:50
 **/
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        if (shippingMapper.insert(shipping) > 0) {
            Map map = Maps.newHashMap();
            map.put("shippingId", shipping.getId());
            return ServerResponse.createBySuccess("新建地址成功", map);
        }
        return ServerResponse.createByErrorMessage("新建地址失败");
    }

    @Override
    public ServerResponse del(Integer userId, Integer shippingId) {
        if (shippingMapper.deleteByUserIdAndShippingId(userId, shippingId) > 0) {
            return ServerResponse.createBySuccessMessage("删除地址成功");
        }
        return ServerResponse.createByErrorMessage("删除地址失败");
    }

    @Override
    public ServerResponse update(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        if (shippingMapper.updateByShippingIdAndUserId(shipping) > 0) {
            return ServerResponse.createBySuccessMessage("更新地址成功");
        }
        return ServerResponse.createByErrorMessage("更新地址失败");
    }

    @Override
    public ServerResponse select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByUserIdAndShippingId(userId, shippingId);
        if (shipping == null) {
            return ServerResponse.createByErrorMessage("没有该地址");
        }
        return ServerResponse.createBySuccess("查询成功", shipping);
    }

    @Override
    public ServerResponse list(Integer userId, Integer current, Integer size) {
        PageHelper.startPage(current, size);
        List<Shipping> shippingList = shippingMapper.selectListByUserId(userId);
        PageInfo pageResult = new PageInfo(shippingList);
        return ServerResponse.createBySuccess(pageResult);
    }
}
