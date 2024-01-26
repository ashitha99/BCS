package com.lms.bcs.Repository;

import com.lms.bcs.Entity.Copy;
import org.hibernate.secure.internal.JaccSecurityListener;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CopyRepository extends JpaRepository<Copy,Long> {
}
