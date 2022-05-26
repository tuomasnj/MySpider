package job.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;

@Component
public class JobProcessor implements PageProcessor {
    String url = "https://search.51job.com/list/070200,000000,0000,00,9,99," +
            "java,2,1.html?lang=c&postchannel=0000&workyear=99&cotype=99&degreefrom" +
            "=99&jobterm=99&companysize=99&ord_field=0&dibiaoid=0&line=&welfare=";

    @Override
    public void process(Page page) {
        String html = page.getHtml().toString();
        System.out.println(111);
    }

    private Site site = Site.me()
            .setCharset("GBK") // 设置编码
            .setTimeOut(10*1000) // 设置超时时间
            .setRetrySleepTime(3000) // 设置重试的间隔时间
            .setRetryTimes(3); // 设置重试的次数
    @Override
    public Site getSite() {
        return site;
    }

    // initialDelay当任务启动后, 等多久执行方法
    // fixedDelay每隔多久执行方法
    @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 1000)
    public void process() {
        Spider.create(new JobProcessor())
                .addUrl(url)
                // 设置Scheduler
                .setScheduler(new QueueScheduler()
                // 设置Bloom去重
                .setDuplicateRemover(new BloomFilterDuplicateRemover(200000)))
                .thread(10)
                .run();
    }
}
