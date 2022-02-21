package com.kickstarter.viewmodels;

import android.support.annotation.NonNull;

import com.kickstarter.KSRobolectricTestCase;
import com.kickstarter.factories.ApiExceptionFactory;
import com.kickstarter.factories.ConfigFactory;
import com.kickstarter.libs.Environment;
import com.kickstarter.services.ApiClientType;
import com.kickstarter.services.MockApiClient;
import com.kickstarter.services.apiresponses.AccessTokenEnvelope;
import com.kickstarter.services.apiresponses.ErrorEnvelope;

import org.junit.Test;

import rx.Observable;
import rx.observers.TestSubscriber;

public class SignupViewModelTest extends KSRobolectricTestCase {

  @Test
  public void testSignupViewModel_FormValidation() {
    final Environment environment = environment();
    environment.currentConfig().config(ConfigFactory.config());
    final SignupViewModel vm = new SignupViewModel(environment);

    final TestSubscriber<Boolean> formIsValidTest = new TestSubscriber<>();
    vm.outputs.formIsValid().subscribe(formIsValidTest);

    vm.inputs.fullName("brandon");
    formIsValidTest.assertNoValues();

    vm.inputs.email("incorrect@kickstarter");
    formIsValidTest.assertNoValues();

    vm.inputs.password("danisawesome");
    formIsValidTest.assertValues(false);

    vm.inputs.email("hello@kickstarter.com");
    formIsValidTest.assertValues(false, true);
  }

  @Test
  public void testSignupViewModel_SuccessfulSignup() {
    final SignupViewModel vm = new SignupViewModel(environment());

    final TestSubscriber<Void> signupSuccessTest = new TestSubscriber<>();
    vm.outputs.signupSuccess().subscribe(signupSuccessTest);

    final TestSubscriber<Boolean> formSubmittingTest = new TestSubscriber<>();
    vm.outputs.formSubmitting().subscribe(formSubmittingTest);

    vm.inputs.fullName("brandon");
    vm.inputs.email("hello@kickstarter.com");
    vm.inputs.email("incorrect@kickstarter");
    vm.inputs.password("danisawesome");
    vm.inputs.sendNewslettersClick(true);

    vm.inputs.signupClick();

    formSubmittingTest.assertValues(true, false);
    signupSuccessTest.assertValueCount(1);
    koalaTest.assertValues("User Signup", "Signup Newsletter Toggle", "Login", "New User");
  }

  @Test
  public void testSignupViewModel_ApiValidationError() {
    final ApiClientType apiClient = new MockApiClient() {
      @Override
      public @NonNull Observable<AccessTokenEnvelope> signup(final @NonNull String name, final @NonNull String email,
        final @NonNull String password, final @NonNull String passwordConfirmation, final boolean sendNewsletters) {
        return Observable.error(ApiExceptionFactory.apiError(
          ErrorEnvelope.builder().httpCode(422).build()
        ));
      }
    };

    final Environment environment = environment().toBuilder().apiClient(apiClient).build();
    final SignupViewModel vm = new SignupViewModel(environment);

    final TestSubscriber<Void> signupSuccessTest = new TestSubscriber<>();
    vm.outputs.signupSuccess().subscribe(signupSuccessTest);

    final TestSubscriber<String> signupErrorTest = new TestSubscriber<>();
    vm.errors.signupError().subscribe(signupErrorTest);

    final TestSubscriber<Boolean> formSubmittingTest = new TestSubscriber<>();
    vm.outputs.formSubmitting().subscribe(formSubmittingTest);

    vm.inputs.fullName("brandon");
    vm.inputs.email("hello@kickstarter.com");
    vm.inputs.email("incorrect@kickstarter");
    vm.inputs.password("danisawesome");
    vm.inputs.sendNewslettersClick(true);

    vm.inputs.signupClick();

    formSubmittingTest.assertValues(true, false);
    signupSuccessTest.assertValueCount(0);
    signupErrorTest.assertValueCount(1);
    koalaTest.assertValues("User Signup", "Signup Newsletter Toggle", "Errored User Signup");
  }

  @Test
  public void testSignupViewModel_ApiError() {
    final ApiClientType apiClient = new MockApiClient() {
      @Override
      public @NonNull Observable<AccessTokenEnvelope> signup(final @NonNull String name, final @NonNull String email,
        final @NonNull String password, final @NonNull String passwordConfirmation, final boolean sendNewsletters) {
        return Observable.error(ApiExceptionFactory.badRequestException());
      }
    };

    final Environment environment = environment().toBuilder().apiClient(apiClient).build();
    final SignupViewModel vm = new SignupViewModel(environment);

    final TestSubscriber<Void> signupSuccessTest = new TestSubscriber<>();
    vm.outputs.signupSuccess().subscribe(signupSuccessTest);

    final TestSubscriber<String> signupErrorTest = new TestSubscriber<>();
    vm.errors.signupError().subscribe(signupErrorTest);

    final TestSubscriber<Boolean> formSubmittingTest = new TestSubscriber<>();
    vm.outputs.formSubmitting().subscribe(formSubmittingTest);

    vm.inputs.fullName("brandon");
    vm.inputs.email("hello@kickstarter.com");
    vm.inputs.email("incorrect@kickstarter");
    vm.inputs.password("danisawesome");
    vm.inputs.sendNewslettersClick(true);

    vm.inputs.signupClick();

    formSubmittingTest.assertValues(true, false);
    signupSuccessTest.assertValueCount(0);
    signupErrorTest.assertValueCount(1);
    koalaTest.assertValues("User Signup", "Signup Newsletter Toggle", "Errored User Signup");
  }
}
