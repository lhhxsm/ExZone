package com.exzone.lib.rxjava.transformers;

import android.support.annotation.NonNull;
import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public final class Transformers {
  private Transformers() {
  }

  /**
   * 将从一个observable发出的`null'值转换为`theDefault`。
   */
  public static @NonNull <T> CoalesceTransformer<T> coalesce(final @NonNull T theDefault) {
    return new CoalesceTransformer<>(theDefault);
  }

  /**
   * 完成时发射
   */
  public static @NonNull <T> CompletedTransformer<T> completed() {
    return new CompletedTransformer<>();
  }

  /**
   * 抛出错误时发射
   */
  public static @NonNull <T> ErrorsTransformer<T> errors() {
    return new ErrorsTransformer<>();
  }

  /**
   * 通过链接`onErrorResumeNext`来防止可观察到的错误。
   */
  public static <T> NeverErrorTransformer<T> neverError() {
    return new NeverErrorTransformer<>();
  }

  /**
   * Prevents an observable from error by chaining `onErrorResumeNext`,
   * and any errors that occur will be piped into the supplied errors publish
   * subject. `null` values will never be sent to the publish subject.
   *
   * @deprecated Use {@link Observable#materialize()} instead.
   */
  @Deprecated public static <T> NeverErrorTransformer<T> pipeErrorsTo(
      final @NonNull PublishSubject<Throwable> errorSubject) {
    return new NeverErrorTransformer<>(new Action1<Throwable>() {
      @Override public void call(Throwable throwable) {
        errorSubject.onNext(throwable);
      }
    });
  }

  /**
   * Prevents an observable from error by chaining `onErrorResumeNext`,
   * and any errors that occur will be piped into the supplied errors action.
   * `null` values will never be sent to the publish subject.
   *
   * @deprecated Use {@link Observable#materialize()} instead.
   */
  @Deprecated public static <T> NeverErrorTransformer<T> pipeErrorsTo(
      final @NonNull Action1<Throwable> errorAction) {
    return new NeverErrorTransformer<>(errorAction);
  }

  /**
   * Emits the latest value of the source observable whenever the `when`
   * observable emits.
   */
  public static <S, T> TakeWhenTransformer<S, T> takeWhen(final @NonNull Observable<T> when) {
    return new TakeWhenTransformer<>(when);
  }

  /**
   * Emits the latest value of the source `when` observable whenever the
   * `when` observable emits.
   */
  public static <S, T> TakePairWhenTransformer<S, T> takePairWhen(
      final @NonNull Observable<T> when) {
    return new TakePairWhenTransformer<>(when);
  }

  /**
   * Zips two observables up into an observable of pairs.
   */
  public static <S, T> ZipPairTransformer<S, T> zipPair(final @NonNull Observable<T> second) {
    return new ZipPairTransformer<>(second);
  }

  /**
   * Emits the latest values from two observables whenever either emits.
   */
  public static <S, T> CombineLatestPairTransformer<S, T> combineLatestPair(
      final @NonNull Observable<T> second) {
    return new CombineLatestPairTransformer<>(second);
  }

  /**
   * Waits until `until` emits one single item and then switches context to the source. This
   * can be useful to delay work until a user logs in:
   * <p>
   * ```
   * somethingThatRequiresAuth
   * .compose(waitUntil(currentUser.loggedInUser()))
   * .subscribe(show)
   * ```
   */
  @NonNull public static <T, R> WaitUntilTransformer<T, R> waitUntil(
      final @NonNull Observable<R> until) {
    return new WaitUntilTransformer<>(until);
  }

  /**
   * Converts an observable of any type into an observable of `null`s. This is useful for forcing
   * Java's type system into knowing we have a stream of `Void`. Simply doing `.map(__ -> null)`
   * is not enough since Java doesn't know if that is a `null` String, Integer, Void, etc.
   * <p>
   * This transformer allows the following pattern:
   * <p>
   * ```
   * myObservable
   * .compose(takeWhen(click))
   * .compose(ignoreValues())
   * .subscribe(subject::onNext)
   * ```
   */
  @NonNull public static <S> IgnoreValuesTransformer<S> ignoreValues() {
    return new IgnoreValuesTransformer<>();
  }

  /**
   * Emits the number of times the source has emitted for every emission of the source. The
   * first emitted value will be `1`.
   */
  @NonNull public static <T> IncrementalCountTransformer<T> incrementalCount() {
    return new IncrementalCountTransformer<>();
  }

  /**
   * 发出值的可观察值。
   */
  public static @NonNull <T> ValuesTransformer<T> values() {
    return new ValuesTransformer<>();
  }

  /**
   * 如果在主线程上调用，请立即调度工作。 否则通过将工作添加到消息队列中延迟其执行，在那里它将在主线程上执行。
   * <p>
   * 这对RecyclerViews特别有用; 如果这些视图中的预订延迟了一帧，则视图会临时显示已回收的内容和帧速率。
   * 为了解决这个问题，我们可以使用`observeForUI（）`立即执行工作，而不是等待一个帧。
   */
  public static @NonNull <T> ObserveForUITransformer<T> observeForUI() {
    return new ObserveForUITransformer<>();
  }
}
