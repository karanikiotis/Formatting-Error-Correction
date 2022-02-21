package com.alorma.github.presenter.repos;

import com.alorma.github.presenter.BaseRxPresenter;
import com.alorma.github.presenter.View;

import java.util.List;

import core.repositories.Repo;
import core.repository.GenericRepository;
import rx.Scheduler;

public class AuthMembershipRepositoriesPresenter
        extends BaseRxPresenter<String, List<Repo>, View<List<Repo>>> {

  public AuthMembershipRepositoriesPresenter(
          Scheduler mainScheduler, Scheduler ioScheduler,
          GenericRepository<String, List<Repo>> repository) {
    super(mainScheduler, ioScheduler, repository);
  }
}
