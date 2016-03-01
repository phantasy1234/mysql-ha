package net.vickymedia.mysql.agent.service;

import net.vickymedia.mysql.agent.model.Biz;

import java.util.List;

/**
 * User: weijie.song
 * Date: 16/2/14 下午2:11
 */
public interface BizService {
	void createBiz(Biz biz);

	List<Biz> getBizList(String name, Integer page, Integer size);

	void updateBiz(Biz biz);
}
