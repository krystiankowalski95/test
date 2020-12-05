package pl.lodz.p.it.ssbd2020.ssbd05.web.mok;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2020.ssbd05.dto.mok.AccountDTO;
import pl.lodz.p.it.ssbd2020.ssbd05.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2020.ssbd05.exceptions.ValidationException;
import pl.lodz.p.it.ssbd2020.ssbd05.exceptions.mok.EmailAlreadyExistsException;
import pl.lodz.p.it.ssbd2020.ssbd05.exceptions.mok.LoginAlreadyExistsException;
import pl.lodz.p.it.ssbd2020.ssbd05.mok.endpoints.interfaces.AddAccountEndpointLocal;
import pl.lodz.p.it.ssbd2020.ssbd05.utils.ResourceBundles;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Kontroler odpowiedzialny za tworzenie przez administratora konta użytkownika
 */
@Log
@Getter
@Setter
@Named
@RequestScoped
public class AddAccountController {

    @Inject
    private AddAccountEndpointLocal addAccountEndpoint;

    private String login;
    private String password;
    private String confirmPassword;
    private String firstname;
    private String lastname;
    private String emailAddress;
    private List<String> accessLevels = new ArrayList<>();
    private boolean active;

    /**
     * Metoda odpowiedzialna za dodawanie konta użytkownika
     */
    public void addAccount() {
        AccountDTO account = new AccountDTO();
        account.setLogin(login);
        account.setPassword(password);
        account.setFirstname(firstname);
        account.setLastname(lastname);
        account.setEmail(emailAddress);
        account.setActive(active);
        account.setAccessLevelCollection(accessLevels);
        account.setConfirmed(true);

        try {
            addAccountEndpoint.addAccount(account);
            ResourceBundles.emitMessageWithFlash(null,"page.registration.account.created");
            clear();
        } catch (LoginAlreadyExistsException ex) {
            ResourceBundles.emitErrorMessageWithFlash(null, ex.getMessage());
            log.log(Level.SEVERE, "Login, "+ LocalDateTime.now(), ex);
        } catch (EmailAlreadyExistsException ex) {
            ResourceBundles.emitErrorMessageWithFlash(null, ex.getMessage());
            log.log(Level.SEVERE, "Email, " + LocalDateTime.now(), ex);
        } catch (ValidationException e) {
            ResourceBundles.emitErrorMessageByPlainText(null, e.getMessage());
            log.severe(e.getMessage() + ", " + LocalDateTime.now());
        } catch (AppBaseException ex) {
            ResourceBundles.emitErrorMessageWithFlash(null, ex.getMessage());
            log.severe(ex.getMessage() + ", " + LocalDateTime.now());
        }
    }

    /**
     * Metoda przenosząca użytkownika na stronę główną aplikacji
     *
     * @return ciąg znaków przekierowujący na stronę główną
     */
    public String goBack() {
        return "home";
    }

    private void clear() {
        login = "";
        password = "";
        confirmPassword = "";
        firstname = "";
        lastname = "";
        emailAddress = "";
        accessLevels = new ArrayList<>();
        active = false;
    }
}
