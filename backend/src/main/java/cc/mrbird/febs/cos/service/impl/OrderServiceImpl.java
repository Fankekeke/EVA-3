package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.Order;
import cc.mrbird.febs.cos.dao.OrderMapper;
import cc.mrbird.febs.cos.service.IOrderService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Override
    public IPage<LinkedHashMap<String, Object>> getOrderPage(Page page, Order order) {
        return baseMapper.getOrderPage(page, order);
    }

    @Override
    public List<LinkedHashMap<String, Object>> getOrderList(String name, Integer flag) {
        return null;
    }
}
