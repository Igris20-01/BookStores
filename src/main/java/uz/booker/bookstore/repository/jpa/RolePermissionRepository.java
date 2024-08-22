package uz.booker.bookstore.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.booker.bookstore.entity.permission.RolePermission;


@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {


}
