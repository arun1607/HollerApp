package com.holler.holler_service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.holler.bean.UpdateUserJobRequestDTO;
import com.holler.bean.UserDTO;
import com.holler.bean.UserJobDTO;
import com.holler.holler_dao.JobDao;
import com.holler.holler_dao.TagDao;
import com.holler.holler_dao.UserDao;
import com.holler.holler_dao.common.HollerConstants;
import com.holler.holler_dao.entity.Jobs;
import com.holler.holler_dao.entity.Tags;
import com.holler.holler_dao.entity.User;
import com.holler.holler_dao.entity.enums.NotificationType;
import com.holler.holler_dao.entity.enums.UserJobStatusType;
import com.holler.holler_dao.util.CommonUtil;

@Service
public class JobServiceImpl implements JobService{

	@Autowired
	JobDao jobDao;

	@Autowired
	TagDao tagDao;

	@Autowired
	UserDao userDao;

	@Autowired
	NotificationService notificationService;
	
	@Autowired
	TokenService tokenService;
	
	@Transactional
	public Map<String, Object> postJob(UserJobDTO userJobDTO, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		   if(tokenService.isValidToken(request)){
		 //if(Boolean.TRUE){
			Jobs job = UserJobDTO.constructJobToPost(userJobDTO);
			userJobDTO.setJobAddress(job.getJobAddress());
			job.setUser(userDao.findById(userJobDTO.getUserId()));
			Set<Tags> tags = new HashSet<Tags>(tagDao.findbyIds(userJobDTO.getTags()));
			job.setTags(tags);
			if(CommonUtil.isNotNull(userJobDTO.getJobId()) && userJobDTO.getJobId() != 0){
				jobDao.update(job);
				notificationService.createJobUpdateNotification(userJobDTO.getTags(), userJobDTO.getUserId(), job.getId());
			}else{
				jobDao.save(job);
				notificationService.createJobPostNotification(userJobDTO.getTags(), userJobDTO.getUserId(), job.getId());
				userJobDTO.setJobId(job.getId());
			}
			result.put(HollerConstants.STATUS, HollerConstants.SUCCESS);
			result.put(HollerConstants.RESULT, userJobDTO);
		}else{
			result.put(HollerConstants.STATUS, HollerConstants.FAILURE);
			result.put(HollerConstants.MESSAGE, HollerConstants.TOKEN_VALIDATION_FAILED);
		}
		 return result;
	}

	public Map<String, Object> viewJob(HttpServletRequest request){
		
		Map<String, Object> result = new HashMap<String, Object>();
			if(tokenService.isValidToken(request)){
		 //if(Boolean.TRUE){
			Jobs job = jobDao.findById(Integer.valueOf(request.getHeader("jobId")));
			UserJobDTO jobDTO = UserJobDTO.getJobDtoFromJob(job);
			result.put(HollerConstants.STATUS, HollerConstants.SUCCESS);
			result.put(HollerConstants.RESULT, jobDTO);
		 }else{
			result.put(HollerConstants.STATUS, HollerConstants.FAILURE);
			result.put(HollerConstants.MESSAGE, HollerConstants.TOKEN_VALIDATION_FAILED);
		}
		return result;
	}

	public Map<String, Object> getMyJobs(HttpServletRequest request){
		
		Map<String, Object> result = new HashMap<String, Object>();
		 if(tokenService.isValidToken(request)){
		 //if(Boolean.TRUE){
			List<Jobs> job = jobDao.findAllByUserId(Integer.valueOf(request.getHeader("userId")));
			List<UserJobDTO> jobDTO = UserJobDTO.getJobDtosToViewJobList(job);
			result.put(HollerConstants.STATUS, HollerConstants.SUCCESS);
			result.put(HollerConstants.RESULT, jobDTO);
		 }else{
			result.put(HollerConstants.STATUS, HollerConstants.FAILURE);
			result.put(HollerConstants.MESSAGE, HollerConstants.TOKEN_VALIDATION_FAILED);
		}
		return result;
	}

	public Map<String, Object> getMyPingedJobs(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(tokenService.isValidToken(request)){
		 //if(Boolean.TRUE){
			List<Jobs> job = jobDao.getMyPingedJobs(Integer.valueOf(request.getHeader("userId")));
			List<UserJobDTO> jobDTO = UserJobDTO.getJobDtosToViewJobList(job);
			result.put(HollerConstants.STATUS, HollerConstants.SUCCESS);
			result.put(HollerConstants.RESULT, jobDTO);
		 }else{
			result.put(HollerConstants.STATUS, HollerConstants.FAILURE);
			result.put(HollerConstants.MESSAGE, HollerConstants.TOKEN_VALIDATION_FAILED);
		}
		return result;
	}


	public Map<String, Object> getUsersAcceptedJob(HttpServletRequest request){
		Map<String, Object> result = new HashMap<String, Object>();
		if(tokenService.isValidToken(request)){
		//if(Boolean.TRUE){
			List<User> users = jobDao.getUserAcceptedJobs(Integer.valueOf(request.getHeader("jobId")));
			List<UserDTO> userDTOs = UserDTO.constructUserDTOsForAcceptedJObs(users);
			result.put(HollerConstants.STATUS, HollerConstants.SUCCESS);
			result.put(HollerConstants.RESULT, userDTOs);
		 }else{
			result.put(HollerConstants.STATUS, HollerConstants.FAILURE);
			result.put(HollerConstants.MESSAGE, HollerConstants.TOKEN_VALIDATION_FAILED);
		}
		return result;
	}

	public Map<String, Object> searchJobsByTag(HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(tokenService.isValidToken(request)){
		 //if(Boolean.TRUE){
			List<Jobs> jobs = jobDao.searchJobsByTag(request.getHeader("tag"));
			List<UserJobDTO> jobDTOs = UserJobDTO.getJobDtosToViewJobList(jobs);
			result.put(HollerConstants.STATUS, HollerConstants.SUCCESS);
			result.put(HollerConstants.RESULT, jobDTOs);
		 }else{
			result.put(HollerConstants.STATUS, HollerConstants.FAILURE);
			result.put(HollerConstants.MESSAGE, HollerConstants.TOKEN_VALIDATION_FAILED);
		}
		return result;
	}

	public Map<String, Object> searchJobsByTagIds(Set<Integer> tagIds, Integer userId, HttpServletRequest request) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		if(tokenService.isValidToken(request)){
		 //if(Boolean.TRUE){
			List<Jobs> jobs = jobDao.searchJobsByTagIds(tagIds);
			User loggedInUser = userDao.findById(userId);
			List<UserJobDTO> jobDTOs = UserJobDTO.getJobIdAndTitleByDiscoveryPreference(jobs, loggedInUser);
			result.put(HollerConstants.STATUS, HollerConstants.SUCCESS);
			result.put(HollerConstants.RESULT, jobDTOs);
		 }else{
			result.put(HollerConstants.STATUS, HollerConstants.FAILURE);
			result.put(HollerConstants.MESSAGE, HollerConstants.TOKEN_VALIDATION_FAILED);
		}
		return result;
	}

	@Transactional
	public Map<String, Object> acceptOrUnacceptJob(UpdateUserJobRequestDTO updateUserJobRequestDTO, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(tokenService.isValidToken(request)){
		// if(Boolean.TRUE){
			 Integer jobId = updateUserJobRequestDTO.getJobId();
			 Integer userId = updateUserJobRequestDTO.getUserId();
			 UserJobStatusType status = UserJobStatusType.valueOf(updateUserJobRequestDTO.getStatus());
				Jobs job = jobDao.findById(jobId);
				if(UserJobStatusType.ACCEPTED == status){
					jobDao.acceptJob(userId, jobId, status);
					notificationService.createNotification(userId, job.getUser().getId(), NotificationType.AcceptJob, Boolean.FALSE, Boolean.FALSE, job.getId());
				}else if(UserJobStatusType.UNACCEPT == status){
					jobDao.unAcceptJob(userId, jobId);
					notificationService.createNotification(userId, job.getUser().getId(), NotificationType.UnAcceptJob, Boolean.FALSE, Boolean.FALSE, job.getId());
				}
			result.put(HollerConstants.STATUS, HollerConstants.SUCCESS);
			result.put(HollerConstants.RESULT, Boolean.TRUE);
		 }else{
			result.put(HollerConstants.STATUS, HollerConstants.FAILURE);
			result.put(HollerConstants.MESSAGE, HollerConstants.TOKEN_VALIDATION_FAILED);
		}
		return result;
	}

	@Transactional
	public Map<String, Object> grantOrUnGrantJob(UpdateUserJobRequestDTO updateUserJobRequestDTO, HttpServletRequest request) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		if(tokenService.isValidToken(request)){
		// if(Boolean.TRUE){
			 Integer jobId = updateUserJobRequestDTO.getJobId();
			 Integer userId = updateUserJobRequestDTO.getUserId();
			 UserJobStatusType status = UserJobStatusType.valueOf(updateUserJobRequestDTO.getStatus());
			if(UserJobStatusType.GRANTED == status){
				jobDao.grantOrUnGrantJob(userId, jobId, status);
				notificationService.createNotification(userId, userId, NotificationType.GrantJob, Boolean.FALSE, Boolean.FALSE, jobId);
			}else if(UserJobStatusType.UNGRANT == status){
				jobDao.grantOrUnGrantJob(userId, jobId, UserJobStatusType.ACCEPTED);
				notificationService.createNotification(userId, userId, NotificationType.UnGrantJob, Boolean.FALSE, Boolean.FALSE, jobId);
			}
			result.put(HollerConstants.STATUS, HollerConstants.SUCCESS);
			result.put(HollerConstants.RESULT, Boolean.TRUE);
		 }else{
			result.put(HollerConstants.STATUS, HollerConstants.FAILURE);
			result.put(HollerConstants.MESSAGE, HollerConstants.TOKEN_VALIDATION_FAILED);
		}
		return result;
	}
}
