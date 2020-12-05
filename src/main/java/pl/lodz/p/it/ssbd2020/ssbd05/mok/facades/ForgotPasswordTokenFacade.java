package pl.lodz.p.it.ssbd2020.ssbd05.mok.facades;

import org.eclipse.persistence.exceptions.DatabaseException;
import pl.lodz.p.it.ssbd2020.ssbd05.abstraction.AbstractFacade;
import pl.lodz.p.it.ssbd2020.ssbd05.entities.mok.ForgotPasswordToken;
import pl.lodz.p.it.ssbd2020.ssbd05.exceptions.AppBaseException;
import pl.lodz.p.it.ssbd2020.ssbd05.exceptions.io.database.AppOptimisticLockException;
import pl.lodz.p.it.ssbd2020.ssbd05.exceptions.io.database.DatabaseConnectionException;
import pl.lodz.p.it.ssbd2020.ssbd05.interceptors.TrackerInterceptor;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.*;
import java.util.List;
import java.util.Optional;

/**
 * fasada tokenu do resetowania hasła - ForgotPasswordTokenFacade
 */
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
@LocalBean
@Interceptors(TrackerInterceptor.class)
public class ForgotPasswordTokenFacade extends AbstractFacade<ForgotPasswordToken> {

    @PersistenceContext(unitName = "ssbd05mokPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    /**
     * Konstruktor bezparametrowy fasady ForgotPasswordToken
     */
    public ForgotPasswordTokenFacade() {
        super(ForgotPasswordToken.class);
    }

    @Override
    @PermitAll
    public void create(ForgotPasswordToken entity) throws AppBaseException {
        try {
            super.create(entity);
        } catch (DatabaseException | PersistenceException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    @Override
    @DenyAll
    public void edit(ForgotPasswordToken entity) throws AppBaseException {
        try {
            super.edit(entity);
        } catch (OptimisticLockException e) {
            throw new AppOptimisticLockException(e);
        } catch (DatabaseException | PersistenceException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    @Override
    @PermitAll
    public void remove(ForgotPasswordToken entity) throws AppBaseException {
        try {
            super.remove(entity);
        } catch (OptimisticLockException e) {
            throw new AppOptimisticLockException(e);
        } catch (DatabaseException | PersistenceException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    @Override
    @DenyAll
    public Optional<ForgotPasswordToken> find(Object id) {
        return super.find(id);
    }

    @Override
    @PermitAll
    public List<ForgotPasswordToken> findAll() throws AppBaseException {
        try {
            return super.findAll();
        } catch (DatabaseException | PersistenceException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    /**
     * Znajdź po hashu
     *
     * @param hash hash
     * @return Optional ForgotPasswordToken
     * @throws AppBaseException Wyjątek aplikacyjny
     */
    @PermitAll
    public Optional<ForgotPasswordToken> findByHash(String hash) throws AppBaseException {
        try {
            return Optional.ofNullable(this.em.createNamedQuery("ForgotPasswordToken.findByHash", ForgotPasswordToken.class)
                    .setParameter("hash", hash).getSingleResult());
        } catch (NoResultException e) {
            throw new AppBaseException("error.default");
        } catch (DatabaseException | PersistenceException e) {
            throw new DatabaseConnectionException(e);
        }
    }

    @Override
    @DenyAll
    public int count() {
        return super.count();
    }
}
