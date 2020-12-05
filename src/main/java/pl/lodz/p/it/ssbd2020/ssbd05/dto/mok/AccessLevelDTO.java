package pl.lodz.p.it.ssbd2020.ssbd05.dto.mok;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Klasa DTO zawierająca informacje o poziomie dostępu użytkownika.
 * Jej instancje są wykorzystywane w warstwie prezentacji.
 */
@NoArgsConstructor
@AllArgsConstructor
public @Data class AccessLevelDTO {

    private String accessLevel;
    private boolean active;

    @Override
    public String toString(){
        return "pl.lodz.p.it.ssbd2020.ssbd05.dto.mok.AccessLevelDTO";
    }
}
