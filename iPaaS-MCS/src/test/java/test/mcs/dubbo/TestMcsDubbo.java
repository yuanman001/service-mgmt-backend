package test.mcs.dubbo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ai.paas.ipaas.mcs.manage.rest.interfaces.IMemoryCacheServiceManager;
import com.alibaba.dubbo.config.annotation.Reference;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/dubbo/consumer/mcs-consumer.xml"})
public class TestMcsDubbo {
  @Reference
  private IMemoryCacheServiceManager iMemoryCacheServiceManager;

//  @Test
//  public void getFuncList() {
//    String fl = iMemoryCacheServiceManager.getFuncList();
//    System.out.println(fl);
//  }

//  @Test
//  public void createSingle() {
//    long s = System.currentTimeMillis();
//    String param =
//        "{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"applyType\":\"create\",\"serviceId\":\"MCS666\",\"capacity\":\"64\","
//            + "\"haMode\":\"single\",\"serviceName\":\"ym-test-2\"}";
//    System.out.println("----------create-res:\n" + iMemoryCacheServiceManager.create(param));
//    System.out.println("----------开通单例，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
//  }

//  @Test
//  public void createCluster() {
//    long s = System.currentTimeMillis();
//    String param =
//        "{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"applyType\":\"create\",\"serviceId\":\"MCS065\",\"capacity\":\"128\",\"haMode\":\"cluster\"}";
//    System.out.println("----------create-res:\n" + iMemoryCacheServiceManager.create(param));
//    System.out.println("----------开通集群，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
//  }

//@Test
//public void cancel() {
//  long s = System.currentTimeMillis();
//  String param =
//      "{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"applyType\":\"cancel\",\"serviceId\":\"MCS789\",\"capacity\":\"64\",\"haMode\":\"single\"}";
//  System.out.println("----------create-res:\n" + iMemoryCacheServiceManager.cancel(param));
//  System.out.println("----------注销MCS服务，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
//}
  
@Test
public void cancelCluster() {
  long s = System.currentTimeMillis();
  String param =
      "{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"applyType\":\"cancel\",\"serviceId\":\"MCS065\",\"capacity\":\"128\",\"haMode\":\"cluster\"}";
  System.out.println("----------create-res:\n" + iMemoryCacheServiceManager.cancel(param));
  System.out.println("----------开通集群，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
}
  
//  @Test
//  public void createReplication() {
//    long s = System.currentTimeMillis();
//    String param =
//        "{\"userId\":\"FFF49D0D518948D0AB28D7A8EEE25D03\",\"applyType\":\"create\",\"serviceId\":\"106\",\"capacity\":\"32\",\"haMode\":\"replication\"}";
//    System.out.println("----------create-res:\n" + iMemoryCacheServiceManager.create(param));
//    System.out.println("----------开通集群，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
//  }
//
//  @Test
//  public void createSentinel() {
//    long s = System.currentTimeMillis();
//    String param =
//        "{\"userId\":\"FFF49D0D518948D0AB28D7A8EEE25D03\",\"applyType\":\"create\",\"serviceId\":\"106\",\"capacity\":\"32\",\"haMode\":\"sentinel\"}";
//    System.out.println("----------create-res:\n" + iMemoryCacheServiceManager.create(param));
//    System.out.println("----------开通集群，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
//  }

//  @Test
//  public void modify() {
//    long s = System.currentTimeMillis();
//    String param =
//        "{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"applyType\":\"modify\",\"serviceId\":\"MCS567\",\"capacity\":\"128\","
//            + "\"haMode\":\"single\",\"serviceName\":\"ym-test-2\"}";
//    System.out.println("----------modify-res:\n" + iMemoryCacheServiceManager.modify(param));
//    System.out.println("----------修改单例，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
//  }

//  @Test
//  public void modifyCluster() {
//    long s = System.currentTimeMillis();
//    String param =
//        "{\"userId\":\"7E1080DCE0584729BEA8BBAFE6C6CAC8\",\"applyType\":\"modify\",\"serviceId\":\"MCS003\",\"capacity\":\"10240\",\"haMode\":\"cluster\"}";
//    System.out.println("----------modify-res:\n" + iMemoryCacheServiceManager.modify(param));
//    System.out.println("----------修改集群，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
//  }

//  @Test
//  public void stop() {
//    long s = System.currentTimeMillis();
//    String param =
//        "{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"applyType\":\"stop\",\"serviceId\":\"MCS666\",\"capacity\":\"64\","
//            + "\"haMode\":\"single\",\"serviceName\":\"ym-test-1\"}";
//    System.out.println("----------stop-res:\n" + iMemoryCacheServiceManager.stop(param));
//    System.out.println("----------停止单例，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
//  }

//  @Test
//  public void stopCluster() {
//    long s = System.currentTimeMillis();
//    String param =
//        "{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"applyType\":\"stop\",\"serviceId\":\"MCS234\",\"capacity\":\"128\",\"haMode\":\"cluster\"}";
//    System.out.println("----------stop-res:\n" + iMemoryCacheServiceManager.stop(param));
//    System.out.println("----------停止集群，耗时：" + (System.currentTimeMillis() - s));
//  }

//  @Test
//  public void start() {
//    long s = System.currentTimeMillis();
//    String param =
//        "{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"applyType\":\"start\",\"serviceId\":\"MCS789\",\"capacity\":\"64\","
//            + "\"haMode\":\"single\",\"serviceName\":\"ym-test-1\"}";
//    System.out.println("----------start-res:\n" + iMemoryCacheServiceManager.start(param));
//    System.out.println("----------启动单例，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
//  }

//  @Test
//  public void startCluster() {
//    long s = System.currentTimeMillis();
//    String param =
//        "{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"applyType\":\"start\",\"serviceId\":\"MCS234\",\"capacity\":\"128\",\"haMode\":\"cluster\"}";
//    System.out.println("----------restart-res:\n" + iMemoryCacheServiceManager.restart(param));
//    System.out.println("----------重启集群，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
//  }
  
//  @Test
//  public void restart() {
//    long s = System.currentTimeMillis();
//    String param =
//        "{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"applyType\":\"restart\",\"serviceId\":\"MCS789\",\"capacity\":\"64\","
//            + "\"haMode\":\"single\",\"serviceName\":\"ym-test-1\"}";
//    System.out.println("----------restart-res:\n" + iMemoryCacheServiceManager.restart(param));
//    System.out.println("----------重启单例，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
//  }

//  @Test
//  public void restartCluster() {
//    long s = System.currentTimeMillis();
//    String param =
//        "{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"applyType\":\"restart\",\"serviceId\":\"MCS234\",\"capacity\":\"128\",\"haMode\":\"cluster\"}";
//    System.out.println("----------restart-res:\n" + iMemoryCacheServiceManager.restart(param));
//    System.out.println("----------重启集群，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
//  }

//  @Test
//  public void get() {
//    long s = System.currentTimeMillis();
//    String param =
//        "{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"serviceId\":\"MCS123\",\"key\":\"a\"}";
//    System.out.println("----------get-res:\n" + iMemoryCacheServiceManager.get(param));
//    System.out.println("----------获得数据，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
//    long s1 = System.currentTimeMillis();
//  }

//  @Test
//  public void del() {
//    long s = System.currentTimeMillis();
//    String param =
//        "{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"serviceId\":\"MCS123\",\"key\":\"a\"}";
//    System.out.println("----------del-res:\n" + iMemoryCacheServiceManager.del(param));
//    System.out.println("----------删除数据，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
//    long s1 = System.currentTimeMillis();
//  }

//  @Test
//  public void clean() {
//    long s = System.currentTimeMillis();
//    String param = "{\"userId\":\"6C4F4DBA96294DDCBC5DBBF2CAD442B5\",\"serviceId\":\"MCS234\"}";
//    System.out.println("----------clean-res:\n" + iMemoryCacheServiceManager.clean(param));
//    System.out.println("----------清空数据，耗时：" + (System.currentTimeMillis() - s)/1000 + "秒");
//  }

}
