/*
 * Copyright 2016 lizhaotailang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.marktony.zhihudaily.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.marktony.zhihudaily.data.GuokrHandpickContentResult;

/**
 * Created by lizhaotailang on 2017/6/15.
 *
 * Interface for database access on {@link GuokrHandpickContentResult} related operations.
 */

@Dao
public interface GuokrHandpickContentDao {

    @Query("SELECT * FROM guokr_handpick_content WHERE id = :id")
    GuokrHandpickContentResult queryContentById(int id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(GuokrHandpickContentResult content);

    @Update
    void update(GuokrHandpickContentResult content);

    @Delete
    void delete(GuokrHandpickContentResult item);

}
