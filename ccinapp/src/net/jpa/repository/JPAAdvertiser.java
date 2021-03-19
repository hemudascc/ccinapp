package net.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.persist.bean.Advertiser;

public interface JPAAdvertiser extends JpaRepository<Advertiser, Long> {

}
