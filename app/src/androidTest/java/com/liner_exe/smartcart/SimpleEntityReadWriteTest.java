package com.liner_exe.smartcart;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.liner_exe.data.local.AppDatabase;
import com.liner_exe.data.local.dao.ProductDao;
import com.liner_exe.data.local.entities.ProductEntity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {
    private ProductDao productDao;
    private AppDatabase db;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        productDao = db.productDao();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        ProductEntity product = new ProductEntity();
        product.name = "Milk";
        productDao.insert(product);
        List<ProductEntity> byName = productDao.findByName("Milk");
        assertThat(byName.get(0).name, equalTo(product.name));
    }
}
