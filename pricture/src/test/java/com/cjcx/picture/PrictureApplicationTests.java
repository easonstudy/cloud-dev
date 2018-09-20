package com.cjcx.picture;

import com.cjcx.picture.service.IPictureService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PrictureApplicationTests {

	@Autowired
	IPictureService pictureService;

	@Test
	public void uploadPicture() throws Exception {
		String url = "http://120.79.210.194/images/receipt_audit/13a05bbf-cb91-467c-acde-7ee3cab0b229.png";
		pictureService.uploadPicture(url, 0);
	}

}
