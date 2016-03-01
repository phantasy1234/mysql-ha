package net.vickymedia.mysql.agent.resource;

import net.vickymedia.mus.dto.OperationResult;
import net.vickymedia.mysql.agent.model.Biz;
import net.vickymedia.mysql.agent.service.BizService;
import net.vickymedia.mysql.agent.util.OperationResultBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * User: weijie.song
 * Date: 16/2/14 下午1:55
 */
@RestController
public class Controller {
	@Resource
	BizService bizService;

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public OperationResult createBiz(@RequestBody Biz biz) {
		bizService.createBiz(biz);
		return OperationResultBuilder.success();
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public OperationResult updateBiz(@RequestBody Biz biz) {
		bizService.updateBiz(biz);
		return OperationResultBuilder.success();
	}

	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<Biz> getBizList(@RequestParam(value = "name", required = false, defaultValue = "") String name,
			@RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
			@RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {

		return bizService.getBizList(name, page, size);
	}

}
