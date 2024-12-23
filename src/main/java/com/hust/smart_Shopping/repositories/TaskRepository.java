package com.hust.smart_Shopping.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.smart_Shopping.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {

}
