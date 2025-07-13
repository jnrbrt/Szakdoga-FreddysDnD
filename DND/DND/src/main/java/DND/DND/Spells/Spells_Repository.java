package DND.DND.Spells;

import DND.DND.features.Features;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface Spells_Repository extends JpaRepository<Spells, Long>
{
    @Query("SELECT s FROM Spells s WHERE s.name = :name AND s.email = :email")
    Spells findSpellsByCharacter(@Param("name") String name, @Param("email") String email);

    @Modifying
    @Query("DELETE FROM Spells s WHERE s.name = :name AND s.email = :email")
    void deleteSpellsByNameAndEmail(@Param("name") String name, @Param("email") String email);
}
