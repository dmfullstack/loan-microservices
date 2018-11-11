package se.loan.repository.domain.blacklist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
interface BlacklistRepository extends JpaRepository<Blacklist, String> {

    @Query("select case when count(bl)=0 then false else true end " +
            "from Blacklist bl where bl.registrationNumber=:regNo")
    boolean exists(@Param("regNo") String registrationNumber);


}