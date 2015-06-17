package com.beam.exercise.dsl.test

import org.mockito.AdditionalAnswers
import org.mockito.Matchers
import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import org.mockito.stubbing.OngoingStubbing
import org.scalatest.mock.MockitoSugar

import scala.reflect.ClassTag

trait EvenMoreSugar extends MockitoSugar {

  implicit def theStubbed[T](c: => T): Stubbed[T] = new Stubbed(c)

  implicit def pimpedInvocationOnMock(im: InvocationOnMock): PimpedInvocationOnMock = new PimpedInvocationOnMock(im)

  class Stubbed[T](c: => T) {
    def returns(t: T, t2: T*): OngoingStubbing[T] = {
      if (t2.isEmpty)
        Mockito.when(c).thenReturn(t)
      else
        t2.foldLeft(Mockito.when(c).thenReturn(t)) {
          (res, cur) => res.thenReturn(cur)
        }
    }

    def returnsArgument(position: Int = 0): OngoingStubbing[T] = Mockito.when(c).then(AdditionalAnswers.returnsArgAt(position))

    def answers(f: InvocationOnMock => T) = Mockito.when(c).thenAnswer(new RiddleMeThis[T](f))

    def throws(ex: Throwable) = Mockito.when(c).thenThrow(ex)
  }

  class PimpedInvocationOnMock(im: InvocationOnMock) {
    def arg[A: ClassTag](idx: Int): A = im.getArguments()(idx) match {
      case a if scala.reflect.classTag[A].runtimeClass.isInstance(a) => a.asInstanceOf[A]
      case _ => throw new IllegalArgumentException("No argument of expected type at idx: " + idx)
    }
  }

  private class RiddleMeThis[T](f: InvocationOnMock => T) extends Answer[T] {
    def answer(invocation: InvocationOnMock): T = f(invocation)
  }

  def any[T](implicit mf: Manifest[T]): T = Matchers.any(mf.runtimeClass.asInstanceOf[Class[T]])

  def same[T](t: T): T = Matchers.eq(t)

  def oneOf[T](mock: T) = Mockito.verify(mock)

  def noneOf[T](mock: T) = Mockito.verify(mock, Mockito.times(0))

  def times[T](mock: T, calls: Int) = Mockito.verify(mock, Mockito.times(calls))

}
