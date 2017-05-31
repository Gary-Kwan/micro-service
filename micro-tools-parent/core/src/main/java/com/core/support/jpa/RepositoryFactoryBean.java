package com.core.support.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

public class RepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
        extends JpaRepositoryFactoryBean<R, T, I> {

    @Override
    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new RepositoryFactory(entityManager);
    }

    private class RepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {
        private final EntityManager entityManager;

        public RepositoryFactory(EntityManager entityManager) {
            super(entityManager);
            this.entityManager = entityManager;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected Object getTargetRepository(RepositoryInformation information) {
//			JpaEntityInformation<T, I> jpaEntityInformation = new JpaMetamodelEntityInformation<T, I>(
//					(Class<T>) information.getDomainType(), entityManager.getMetamodel());
//			return new BaseRepositoryImpl<T, I>(jpaEntityInformation, entityManager);
            return new BaseRepositoryImpl<T, I>((Class<T>) information.getDomainType(), entityManager);
        }

        @Override
        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {

			/*
             * if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
			 * return QueryDslJpaRepository.class; } else { return
			 * SimpleJpaRepository.class; }
			 */
            return BaseRepositoryImpl.class;
        }
    }
}
