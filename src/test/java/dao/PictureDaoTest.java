package dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhou.ssm.dao.PictureDao;
import com.zhou.ssm.entity.Picture;

@RunWith(SpringJUnit4ClassRunner.class) //指定测试用例的运行器 这里是指定了Junit4
@ContextConfiguration("classpath:spring-beans.xml")
public class PictureDaoTest {
	
    @Autowired
    private PictureDao pictureDao;

    @Test
    public void insertPictureTest() {
        Picture picture = null;
        for (int i = 1; i < 2; i++) {
                picture = new Picture();
                picture.setType(i + "");
                picture.setGrade(i + "");
                picture.setPath("upload/20170425_1714107.jpg");
                picture.setTime("2017-04-25 17:15:44");
                picture.setUrl("url");
                pictureDao.insertPicture(picture);
        }
    }

}
