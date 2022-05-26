package job.dao;

import job.pojo.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobInfoDao  extends JpaRepository<JobInfo,Long> {
}
