package com.thoughtworks.rslist.repository;

import com.thoughtworks.rslist.po.UserPo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.CrudRepositoryExtensionsKt;

import java.util.List;

public interface UserRepository extends CrudRepository <UserPo,Integer>{
    @Override
    List<UserPo> findAll();

    UserPo findById(int id);

    void deleteById(int id);


}
