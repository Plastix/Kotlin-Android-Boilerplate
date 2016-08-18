package io.github.plastix.kotlinboilerplate.util

import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

import rx.Scheduler
import rx.android.plugins.RxAndroidPlugins
import rx.android.plugins.RxAndroidSchedulersHook
import rx.plugins.RxJavaHooks
import rx.schedulers.Schedulers

/**
 * This rule registers SchedulerHooks for RxJava and RxAndroid to ensure that subscriptions
 * always subscribeOn and observeOn Schedulers.immediate().
 * Warning, this rule will reset RxAndroidPlugins and RxJavaPlugins before and after each test so
 * if the application code uses RxJava plugins this may affect the behaviour of the testing method.
 *
 *
 * This code is adapted from Ribot's Android Boilerplate (Apache 2 license)
 * https://github.com/ribot/android-boilerplate
 */
class RxSchedulersOverrideRule : TestRule {

    private val rxAndroidSchedulersHook = object : RxAndroidSchedulersHook() {
        override fun getMainThreadScheduler(): Scheduler {
            return Schedulers.immediate()
        }
    }

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                RxAndroidPlugins.getInstance().reset()
                RxAndroidPlugins.getInstance().registerSchedulersHook(rxAndroidSchedulersHook)

                RxJavaHooks.reset()
                RxJavaHooks.setOnIOScheduler { scheduler -> Schedulers.immediate() }
                RxJavaHooks.setOnNewThreadScheduler { scheduler -> Schedulers.immediate() }
                RxJavaHooks.setOnComputationScheduler { scheduler -> Schedulers.immediate() }

                base.evaluate()

                RxAndroidPlugins.getInstance().reset()
                RxJavaHooks.reset()
            }
        }
    }
}