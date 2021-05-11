package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.Evaluation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface EvaluationMapper extends BaseMapper<Evaluation> {

    // 分页查询评价信息
    IPage<LinkedHashMap<String, Object>> getEvaluationPage(Page page, @Param("evaluation") Evaluation evaluation);
}
