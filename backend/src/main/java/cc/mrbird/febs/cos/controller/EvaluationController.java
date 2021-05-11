package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.annotation.Log;
import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.Evaluation;
import cc.mrbird.febs.cos.service.IEvaluationService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/evaluation")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EvaluationController {

    private final IEvaluationService evaluationService;

    @Log("查询订单评价")
    @GetMapping("/page")
    public R page(Page page, Evaluation evaluation) {
        return R.ok(evaluationService.getEvaluationPage(page, evaluation));
    }

    @Log("添加订单评价")
    @PostMapping
    public R save(@RequestBody Evaluation evaluation) {
        return R.ok(evaluationService.save(evaluation));
    }
}
