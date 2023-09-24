package com.example;

import com.example.entity.stop;
import org.apache.ibatis.annotations.Insert;

public interface mapper  {
    @Insert("")
    int add(stop stop);
}
