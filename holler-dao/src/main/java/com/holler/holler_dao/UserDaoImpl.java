package com.holler.holler_dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.holler.holler_dao.entity.enums.UserStatusType;
import com.holler.holler_dao.util.CommonUtil;
import com.holler.holler_dao.util.LazyIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.holler.holler_dao.common.SQLQueryIds;
import com.holler.holler_dao.entity.User;
import com.holler.holler_dao.mapper.UserMapper;

@Repository
public class UserDaoImpl extends BaseDaoImpl<User> implements UserDao {
	
	public UserDaoImpl() {
		super(User.class);
	}
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	QueryDao queryDao;
	
	@PersistenceContext
	EntityManager entityManager;
	
	public boolean authenticateUser(String email, String password){
		String sql = queryDao.getQueryString(SQLQueryIds.AUTHENTICATE_USER);
		List<User> userList = jdbcTemplate.query(sql, new String[]{email, password}, new UserMapper());
		return (null != userList && userList.size() == 1);
	}
	
	public User getByEmailAndPassword(String email, String password){
		String sql = queryDao.getQueryString(SQLQueryIds.AUTHENTICATE_USER);
		List<User> userList = jdbcTemplate.query(sql, new String[]{email, password}, new UserMapper());
		if(userList != null && !userList.isEmpty() && userList.size() == 1){
			return userList.get(0);
		}
		return null;
	}

	public boolean checkIfUserExists(String email, String phoneNumber) {
		List<User> userList = entityManager.createQuery("from " + User.class.getName()
				+ " where (email=:email OR phoneNumber = :phoneNumber) AND status NOT IN (:status)", User.class)
				.setParameter("email", email)
				.setParameter("phoneNumber", phoneNumber)
				.setParameter("status", UserStatusType.DELETED).getResultList();
		if(CommonUtil.notNullAndEmpty(userList)){
			return true;
		}
		return false;
	}

	public List<Object[]> getUserJobs(int requestUserId){
		String sql = queryDao.getQueryString(SQLQueryIds.GET_USER_JOBS);
		Query queryObject = entityManager.createNativeQuery(sql)
				.setParameter("requestUserId", requestUserId);
		List<Object[]> resultList = queryObject.getResultList();
		return resultList;
	}

	public User getByPhoneNumber(String phoneNumber){
		List<User> userList = entityManager.createQuery("from " + User.class.getName()
				+ " where phoneNumber = :phoneNumber AND status NOT IN (:status)", User.class)
				.setParameter("phoneNumber", phoneNumber)
				.setParameter("status", UserStatusType.DELETED).getResultList();
		if(CommonUtil.notNullAndEmpty(userList)){
			return userList.get(0);
		}
		return null;
	}

	public User findByIdWithTags(int userId) {
		List<User> userList = entityManager.createQuery("from " + User.class.getName()
				+ " where id = :userId AND status NOT IN (:status)", User.class)
				.setParameter("userId", userId)
				.setParameter("status", UserStatusType.DELETED).getResultList();
		if(CommonUtil.notNullAndEmpty(userList)) {
			return userList.get(0);
		}
		return null;
	}

}
