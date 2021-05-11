package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.Order;
import cc.mrbird.febs.cos.service.IOrderService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/order")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {

    private final IOrderService orderService;

    @Log("查询订单信息")
    @GetMapping("/page")
    public R page(Page page, Order order) {
        return R.ok(orderService.getOrderPage(page, order));
    }

    @Log("新增订单")
    @PostMapping
    public R save(@RequestBody Order order) {
        order.setCode("ORD-"+ new Date().getTime());
        return R.ok(orderService.save(order));
    }

    @Log("删除订单")
    @DeleteMapping("/{ids}")
    public R delete(@PathVariable("ids") List<Integer> ids) {
        return R.ok(orderService.removeByIds(ids));
    }

}
