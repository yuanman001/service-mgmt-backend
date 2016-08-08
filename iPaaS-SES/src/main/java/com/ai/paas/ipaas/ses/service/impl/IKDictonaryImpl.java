package com.ai.paas.ipaas.ses.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ai.paas.ipaas.ServiceUtil;
import com.ai.paas.ipaas.ses.dao.interfaces.SesUserIndexWordMapper;
import com.ai.paas.ipaas.ses.dao.interfaces.SesUserStopWordMapper;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserIndexWord;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserIndexWordCriteria;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserInstanceCriteria;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserStopWord;
import com.ai.paas.ipaas.ses.dao.mapper.bo.SesUserStopWordCriteria;
import com.ai.paas.ipaas.ses.service.interfaces.IIKDictonary;
import com.ai.paas.ipaas.util.StringUtil;

@Service
public class IKDictonaryImpl implements IIKDictonary {

	private static transient final Logger log = LoggerFactory
			.getLogger(IKDictonaryImpl.class);
	@Autowired
	SqlSessionTemplate sqlSessionTemplate;

	@Override
	public void saveDictonaryWord(List<SesUserIndexWord> indexWordList,
			List<SesUserStopWord> stopWordList, String userId, String serviceId) {
		SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory()
				.openSession(ExecutorType.BATCH, false);
		SesUserIndexWordMapper indexWordMapper = ServiceUtil
				.getMapper(SesUserIndexWordMapper.class);
		SesUserStopWordMapper stopWordMapper = ServiceUtil
				.getMapper(SesUserStopWordMapper.class);

		try {

			SesUserInstanceCriteria userCriteria = new SesUserInstanceCriteria();
			userCriteria.createCriteria().andUserIdEqualTo(userId)
					.andServiceIdEqualTo(serviceId);
			log.info("Start to save index words!....");
			if (null != indexWordList) {
				if (indexWordList.size() > 500) {
					List<SesUserIndexWord> newIndexList = new ArrayList<SesUserIndexWord>();
					for (int i = 0; i < indexWordList.size(); i++) {
						newIndexList.add(indexWordList.get(i));
						if (i != 0 && (i % 500 == 0)) {
							indexWordMapper.insertBatch(newIndexList);
							sqlSession.commit();
							sqlSession.clearCache();
							newIndexList = new ArrayList<SesUserIndexWord>();
						}
					}
					if (newIndexList.size() > 0) {
						indexWordMapper.insertBatch(newIndexList);
						sqlSession.commit();
						sqlSession.clearCache();
					}
				} else if (indexWordList.size() > 0) {
					indexWordMapper.insertBatch(indexWordList);
					sqlSession.commit();
					sqlSession.clearCache();
				}
			}
			if (null != stopWordList) {
				if (stopWordList.size() > 500) {
					List<SesUserStopWord> newStopList = new ArrayList<SesUserStopWord>();
					for (int i = 0; i < stopWordList.size(); i++) {
						newStopList.add(stopWordList.get(i));
						if (i != 0 && (i % 500 == 0)) {
							stopWordMapper.insertBatch(newStopList);
							sqlSession.commit();
							sqlSession.clearCache();
							newStopList = new ArrayList<SesUserStopWord>();
						}
					}
					if (newStopList.size() > 0) {
						stopWordMapper.insertBatch(newStopList);
						sqlSession.commit();
						sqlSession.clearCache();
					}
				} else if (stopWordList.size() > 0) {
					stopWordMapper.insertBatch(stopWordList);
					sqlSession.commit();
					sqlSession.clearCache();
				}
			}
		} catch (Exception e) {
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
	}

	@SuppressWarnings("rawtypes")
	public Map<String, List> getUserDictionary(String userId, String serviceId,
			int start, int rows) {
		Map<String, List> returnMap = new HashMap<String, List>();
		SesUserIndexWordMapper indexWordMapper = ServiceUtil
				.getMapper(SesUserIndexWordMapper.class);
		SesUserStopWordMapper stopWordMapper = ServiceUtil
				.getMapper(SesUserStopWordMapper.class);
		SesUserIndexWordCriteria indexWordCriteria = new SesUserIndexWordCriteria();
		indexWordCriteria.setLimitStart(start <= 1 ? 0 : (start - 1) * rows);
		indexWordCriteria.setLimitEnd(rows);
		indexWordCriteria.createCriteria().andUserIdEqualTo(userId)
				.andServiceIdEqualTo(serviceId);
		List<SesUserIndexWord> allIndexWordList = indexWordMapper
				.selectByExample(indexWordCriteria);
		SesUserStopWordCriteria stopWordCriteria = new SesUserStopWordCriteria();
		stopWordCriteria.setLimitStart(start <= 1 ? 0 : (start - 1) * rows);
		stopWordCriteria.setLimitEnd(rows);
		stopWordCriteria.createCriteria().andUserIdEqualTo(userId)
				.andServiceIdEqualTo(serviceId);
		List<SesUserStopWord> allStopWordList = stopWordMapper
				.selectByExample(stopWordCriteria);
		returnMap.put("allIndexWordList", allIndexWordList);
		returnMap.put("allStopWordList", allStopWordList);
		return returnMap;
	}

	@Override
	public void saveAllIndexWords(String userId, String serviceId, String words) {
		// 先删除所有原来的，再插入新的,这样数据量大时需要批量操作，性能如何
		SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory()
				.openSession(ExecutorType.BATCH, false);
		try {
			SesUserIndexWordMapper indexWordMapper = ServiceUtil
					.getMapper(SesUserIndexWordMapper.class);
			SesUserIndexWordCriteria indexWordCriteria = new SesUserIndexWordCriteria();
			indexWordCriteria.createCriteria().andUserIdEqualTo(userId)
					.andServiceIdEqualTo(serviceId);
			indexWordMapper.deleteByExample(indexWordCriteria);
			sqlSession.commit();
			// 开始插入
			String[] splits = words.split(" ");
			if (null != splits) {
				SesUserIndexWord indexWord = null;
				Timestamp d = new Timestamp(System.currentTimeMillis());
				List<SesUserIndexWord> wordList = new ArrayList<>();
				for (String word : splits) {
					if (StringUtil.isBlank(word)
							|| StringUtil.isBlank(word.trim()))
						continue;
					indexWord = new SesUserIndexWord();
					indexWord.setCreateTime(d);
					indexWord.setUserId(userId);
					indexWord.setServiceId(serviceId);
					indexWord.setWord(word);
					wordList.add(indexWord);
					if (wordList.size() >= 500) {
						indexWordMapper.insertBatch(wordList);
						sqlSession.commit();
						sqlSession.clearCache();
						wordList.clear();
					}
				}
				// 最后提交一下
				indexWordMapper.insertBatch(wordList);
				sqlSession.commit();
			}
		} catch (Exception e) {
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void saveAllStopWords(String userId, String serviceId, String words) {
		// 先删除所有原来的，再插入新的,这样数据量大时需要批量操作，性能如何
		SesUserStopWordMapper stopWordMapper = ServiceUtil
				.getMapper(SesUserStopWordMapper.class);
		SesUserStopWordCriteria stopWordCriteria = new SesUserStopWordCriteria();
		stopWordCriteria.createCriteria().andUserIdEqualTo(userId)
				.andServiceIdEqualTo(serviceId);
		stopWordMapper.deleteByExample(stopWordCriteria);
		// 开始插入
		String[] splits = words.split(" ");
		if (null != splits) {
			SesUserStopWord stopWord = null;
			Timestamp d = new Timestamp(System.currentTimeMillis());
			List<SesUserStopWord> wordList = new ArrayList<>();
			for (String word : splits) {
				if (StringUtil.isBlank(word) || StringUtil.isBlank(word.trim()))
					continue;
				stopWord = new SesUserStopWord();
				stopWord.setCreateTime(d);
				stopWord.setUserId(userId);
				stopWord.setServiceId(serviceId);
				stopWord.setWord(word);
				wordList.add(stopWord);
				if (wordList.size() >= 500) {
					stopWordMapper.insertBatch(wordList);
					wordList.clear();
				}
			}
			// 最后提交一下
			stopWordMapper.insertBatch(wordList);
		}
	}

	@Override
	public String getUserIndexWords(String userId, String serviceId, int start,
			int rows) {
		SesUserIndexWordMapper indexWordMapper = ServiceUtil
				.getMapper(SesUserIndexWordMapper.class);
		SesUserIndexWordCriteria indexWordCriteria = new SesUserIndexWordCriteria();
		indexWordCriteria.setLimitStart(start <= 1 ? 0 : (start - 1) * rows);
		indexWordCriteria.setLimitEnd(rows);
		indexWordCriteria.createCriteria().andUserIdEqualTo(userId)
				.andServiceIdEqualTo(serviceId);
		List<SesUserIndexWord> indexWordList = indexWordMapper
				.selectByExample(indexWordCriteria);
		if (null == indexWordList || indexWordList.size() <= 0)
			return null;
		else {
			StringBuilder sb = new StringBuilder();
			for (SesUserIndexWord indexWord : indexWordList) {
				sb.append(indexWord.getWord()).append(" ");
			}
			return sb.toString().trim();
		}
	}

	@Override
	public String getUserStopWords(String userId, String serviceId, int start,
			int rows) {
		SesUserStopWordMapper stopWordMapper = ServiceUtil
				.getMapper(SesUserStopWordMapper.class);
		SesUserStopWordCriteria stopWordCriteria = new SesUserStopWordCriteria();
		stopWordCriteria.setLimitStart(start <= 1 ? 0 : (start - 1) * rows);
		stopWordCriteria.setLimitEnd(rows);
		stopWordCriteria.createCriteria().andUserIdEqualTo(userId)
				.andServiceIdEqualTo(serviceId);
		List<SesUserStopWord> stopWordList = stopWordMapper
				.selectByExample(stopWordCriteria);
		if (null == stopWordList || stopWordList.size() <= 0)
			return null;
		else {
			StringBuilder sb = new StringBuilder();
			for (SesUserStopWord stopWord : stopWordList) {
				sb.append(stopWord.getWord()).append(" ");
			}
			return sb.toString().trim();
		}
	}

	public void clearAllIndexWords(String userId, String serviceId) {
		SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory()
				.openSession(ExecutorType.BATCH, false);
		try {
			SesUserIndexWordMapper indexWordMapper = ServiceUtil
					.getMapper(SesUserIndexWordMapper.class);
			SesUserIndexWordCriteria indexWordCriteria = new SesUserIndexWordCriteria();
			indexWordCriteria.createCriteria().andUserIdEqualTo(userId)
					.andServiceIdEqualTo(serviceId);
			int total = indexWordMapper.countByExample(indexWordCriteria);

			long count = Math.round(total / 500.0 + 0.5);
			for (int i = 0; i < count; i++) {
				indexWordCriteria.setLimitStart(0);
				indexWordCriteria.setLimitEnd(500);
				indexWordMapper.deleteByExample(indexWordCriteria);
				sqlSession.commit();
				sqlSession.clearCache();
			}
		} catch (Exception e) {
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
	}

	@Override
	public void clearAllStopWords(String userId, String serviceId) {
		SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory()
				.openSession(ExecutorType.BATCH, false);
		try {
			SesUserStopWordMapper stopWordMapper = ServiceUtil
					.getMapper(SesUserStopWordMapper.class);
			SesUserStopWordCriteria stopWordCriteria = new SesUserStopWordCriteria();
			stopWordCriteria.createCriteria().andUserIdEqualTo(userId)
					.andServiceIdEqualTo(serviceId);
			int total = stopWordMapper.countByExample(stopWordCriteria);

			long count = Math.round(total / 500.0 + 0.5);
			for (int i = 0; i < count; i++) {
				stopWordCriteria.setLimitStart(0);
				stopWordCriteria.setLimitEnd(500);
				stopWordMapper.deleteByExample(stopWordCriteria);
				sqlSession.commit();
				sqlSession.clearCache();
			}
		} catch (Exception e) {
			sqlSession.rollback();
		} finally {
			sqlSession.close();
		}
	}

}
