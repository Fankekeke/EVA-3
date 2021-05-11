package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.Evaluation;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IEvaluationService extends IService<Evaluation> {

    // 分页查询评价信息
    IPage<LinkedHashMap<String, Object>> getEvaluationPage(Page page, Evaluation evaluation);
}
