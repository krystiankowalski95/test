package pl.lodz.p.it.ssbd2020.ssbd05.web.mok;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2020.ssbd05.dto.mok.AccountDTO;
import pl.lodz.p.it.ssbd2020.ssbd05.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2020.ssbd05.exceptions.ValidationException;
import pl.lodz.p.it.ssbd2020.ssbd05.exceptions.io.database.AppOptimisticLockException;
import pl.lodz.p.it.ssbd2020.ssbd05.mok.endpoints.interfaces.EditAccountEndpointLocal;
import pl.lodz.p.it.ssbd2020.ssbd05.utils.ResourceBundles;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Kontroler odpowiedzialny za zmianę hasła innego użytkownika przez administratora
 */
@Log
@Named
@ViewScoped
public class ChangeOtherAccountPasswordController implements Serializable {

    @Inject
    private EditAccountEndpointLocal editAccountEndpointLocal;

    @Getter
    @Setter
    private String previousPassword;
    @Getter
    @Setter
    private String newPassword;
    @Getter
    @Setter
    private String newConfirmPassword;
    @Getter
    @Setter
    private AccountDTO accountDTO;

    /**
     * Metoda wczytująca dane wybranego konta do edycji w postaci obiektu typu AccountDTO
     */
    @PostConstruct
    public void init(){
        String selectedLogin = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("selectedLogin");
        try {
            this.accountDTO = editAccountEndpointLocal.findByLogin(selectedLogin);
        } catch (AppBaseException appBaseException) {
            ResourceBundles.emitErrorMessageWithFlash(null, appBaseException.getMessage());
            log.severe(appBaseException.getMessage() + ", " + LocalDateTime.now());
        }
    }

    /**
     * Metoda dokonująca edycji obiektu typu AccountDTO polegającej na ustawieniu w nim nowego hasła
     */
    public void setPassword() {
        try {
            this.accountDTO.setPassword(newPassword);
            editAccountEndpointLocal.changeOtherAccountPassword(accountDTO);
            ResourceBundles.emitMessageWithFlash(null, "page.changepassword.message");
        } catch (AppOptimisticLockException ex) {
            log.severe(ex.getMessage() + ", " + LocalDateTime.now());
            ResourceBundles.emitErrorMessageWithFlash(null, "error.account.optimisticlock");
            goBack();
        } catch (ValidationException e) {
            log.severe(e.getMessage() + ", " + LocalDateTime.now());
            ResourceBundles.emitErrorMessageByPlainText(null, e.getMessage());
        } catch (AppBaseException appBaseException) {
            log.severe(appBaseException.getMessage() + ", " + LocalDateTime.now());
            ResourceBundles.emitErrorMessageWithFlash(null, appBaseException.getMessage());
        }
    }

    /**
     * Metoda przenosząca na stronę ze zmianą hasła wybranego użytkownika z poziomu szczegółów jego konta
     *
     * @return Ciąg znaków, dla którego została zdefiniowana zasada nawigacji w deskryptorze faces-config.xml
     */
    public String redirectToChangePassword() {
        return "changePassword";
    }

    /**
     * Metoda przenosząca na stronę ze szczegółami wybranego konta po pomyślnej zmianie hasła
     *
     * @return Ciąg znaków, dla którego została zdefiniowana zasada nawigacji w deskryptorze faces-config.xml
     */
    public String goBack() {
        return "accountDetails";
    }
}
