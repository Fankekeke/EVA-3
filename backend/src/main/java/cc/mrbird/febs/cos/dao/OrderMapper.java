package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface OrderMapper extends BaseMapper<Order> {

    // 查询订单分页信息
    IPage<LinkedHashMap<String, Object>> getOrderPage(Page page, @Param("order") Order order);

    // 根据用户查询订单
    List<LinkedHashMap<String, Object>> getOrderList(@Param("name") String name, @Param("flag") Integer flag);
}
