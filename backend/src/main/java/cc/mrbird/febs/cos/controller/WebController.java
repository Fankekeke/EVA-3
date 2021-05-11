package cc.mrbird.febs.cos.controller;

import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.Order;
import cc.mrbird.febs.cos.service.*;
import cc.mrbird.febs.system.domain.User;
import cc.mrbird.febs.system.service.UserService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WebController {

    private final IHotelService hotelService;
    private final IRoomTypeService roomTypeService;
    private final IRoomService roomService;
    private final IOrderService orderService;
    private final IEvaluationService evaluationService;
    private final UserService userService;

    @GetMapping("/hotelInfo")
    public R getHotelInfo() {
        return R.ok(hotelService.getById(1));
    }

    @PostMapping("/userAdd")
    public R userAdd(@RequestBody User user) throws Exception { ;
        Integer count = this.userService.count(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (count > 0) {
            return R.ok(0);
        } else {
            this.userService.createUser(user);
            return R.ok(1);
        }
    }

    @GetMapping("/roomList")
    public R roomList(String start, String end) {
        return R.ok(this.roomService.roomList());
    }

    @PostMapping("/order")
    public R orderAdd(@RequestBody Order order) {
        // 根据用户名获取用户ID
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, order.getUsername()));
        order.setCode("ORD-"+ new Date().getTime());
        order.setUserId(user.getUserId());
        return R.ok(orderService.save(order));
    }

    @GetMapping("/orderList")
    public R getOrderList(@RequestParam("name") String name, @RequestParam("flag") Integer flag) {
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,name));
        return R.ok(orderService.list(Wrappers.<Order>lambdaQuery().eq(Order::getUserId, user.getUserId()).eq(flag != -1,Order::getStatus, flag).eq(Order::getDeleteFlag, 0)));
    }
}
