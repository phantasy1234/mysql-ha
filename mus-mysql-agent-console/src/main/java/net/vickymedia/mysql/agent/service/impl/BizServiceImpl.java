package net.vickymedia.mysql.agent.service.impl;

import com.google.common.collect.Lists;
import net.vickymedia.mysql.agent.model.Biz;
import net.vickymedia.mysql.agent.service.BizService;
import net.vickymedia.mysql.agent.zookeeper.ZKClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: weijie.song
 * Date: 16/2/14 下午2:11
 */
@Service
public class BizServiceImpl implements BizService {
	@Autowired
	ZKClient zkClient;

	@Override
	public void createBiz(Biz biz) {
		zkClient.pushData(biz.getName(), biz);
	}

	@Override
	public List<Biz> getBizList(String name, Integer page, Integer size) {
		if (StringUtils.isBlank(name)) {
			return zkClient.getChildren(name, page, size, Biz.class);
		}
		return Lists.newArrayList(zkClient.getData(name, Biz.class));
	}

	@Override
	public void updateBiz(Biz biz) {
		zkClient.setData(biz.getName(), biz);
	}

}
