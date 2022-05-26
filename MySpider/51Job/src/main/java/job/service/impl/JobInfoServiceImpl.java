package job.service.impl;

import job.dao.JobInfoDao;
import job.pojo.JobInfo;
import job.service.JobInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobInfoServiceImpl implements JobInfoService {
    @Autowired
    private JobInfoDao jobInfoDao;

    @Override
    @Transactional
    public void save(JobInfo jobInfo) {
        this.jobInfoDao.save(jobInfo);
    }

    @Override
    public List<JobInfo> findJobInfo(JobInfo jobInfo) {
        //设置查询条件，根据参数jobInfo中的非空字段进行精确查询
        Example example = Example.of(jobInfo);

        //执行查询
        List<JobInfo> ans = this.jobInfoDao.findAll(example);

        return ans;
    }
}
