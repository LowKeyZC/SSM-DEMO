package com.zhou.ssm.controller.admin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhou.ssm.common.Result;
import com.zhou.ssm.common.ResultGenerator;
import com.zhou.ssm.entity.Article;
import com.zhou.ssm.entity.PageBean;
import com.zhou.ssm.service.ArticleService;
import com.zhou.ssm.util.DateUtil;
import com.zhou.ssm.util.ResponseUtil;
import com.zhou.ssm.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Controller
@RequestMapping("/admin/articles")
public class ArticleAdminController {
	
    @Resource
    private ArticleService articleService;
    
    private static final long serialVersionUID = 1L;
    private static final Logger log = Logger.getLogger(ArticleAdminController.class);// 日志文件

    /**
     * 查找相应的数据集合
     *
     * @param page
     * @param rows
     * @param article
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/datagrid", method = RequestMethod.POST)
    public String list(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "rows", required = false) String rows,
            Article article, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<String, Object>();
        if (page != null && rows != null) {
            PageBean pageBean = new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
            map.put("start", pageBean.getStart());
            map.put("size", pageBean.getPageSize());
        }
        if (article != null) {
        	//格式化用户查询搜索:"%str%"
            map.put("articleTitle",StringUtil.formatLike(article.getArticleTitle()));
        }
        List<Article> articleList = articleService.findArticle(map);
        Long total = articleService.getTotalArticle(map);
        JSONObject result = new JSONObject();
        JSONArray jsonArray = JSONArray.fromObject(articleList);
        result.put("rows", jsonArray);
        result.put("total", total);
        log.info("request: admin/article/list , map: " + map.toString());
        ResponseUtil.write(response, result);
        return null;
    }

    /**
     * 添加
     *
     * @param article
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody Article article)
            throws Exception {
        int resultTotal = 0;

        article.setArticleCreateDate(DateUtil.getCurrentDateStr());

        resultTotal = articleService.addArticle(article);

        log.info("request: admin/article/save , " + article.toString());

        if (resultTotal > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("添加失败");
        }
    }

    /**
     * 修改
     *
     * @param article
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public Result update(@RequestBody Article article)
            throws Exception {
        int resultTotal = 0;

        resultTotal = articleService.updateArticle(article);

        log.info("request: admin/article/update , " + article.toString());

        if (resultTotal > 0) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("修改失败");
        }
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{ids}", method = RequestMethod.DELETE)
    @ResponseBody
    public Result delete(@PathVariable("ids") String ids) throws Exception {
        if (ids.length() > 20) {
            return ResultGenerator.genFailResult("ERROR");
        }
        String[] idsStr = ids.split(",");
        for (int i = 0; i < idsStr.length; i++) {
            articleService.deleteArticle(idsStr[i]);
        }

        log.info("request: admin/article/delete , ids: " + ids);
        return ResultGenerator.genSuccessResult();
    }

    /**
     * 根据id查找
     *
     * @param id
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Result findById(@PathVariable("id") String id) throws Exception {
        Article article = articleService.findById(id);
        Result result = ResultGenerator.genSuccessResult();
        result.setData(article);
        log.info("request: admin/article/findById");
        return result;
    }
}
