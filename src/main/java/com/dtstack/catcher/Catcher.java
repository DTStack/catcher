package com.dtstack.catcher;

import java.lang.instrument.Instrumentation;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.field.FieldDescription;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.ClassFileLocator;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.Transformer;
import net.bytebuddy.dynamic.Transformer.ForField;
import net.bytebuddy.dynamic.DynamicType.Builder;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.pool.TypePool;
import net.bytebuddy.utility.JavaModule;

public class Catcher {

	public static void premain(String agentArgs, Instrumentation inst) {

		new AgentBuilder.Default().type(ElementMatchers.named("liangchen.test.LiangMain"))
				.transform(new AgentBuilder.Transformer() {

					@Override
					public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription,
							ClassLoader classLoader, JavaModule module) {
						System.out.println("method");
						return builder.method(ElementMatchers.named("testBuddy"))
								.intercept(MethodDelegation.to(TimeInterceptor.class));
					}
				}).installOn(inst);
	}

	public static void main(String[] args) throws Exception {
		ByteBuddyAgent.install();
		// new ByteBuddy()
		// .redefine(Bar.class)
		// .name(Foo.class.getName())
		// .make()
		// .load(Foo.class.getClassLoader(),
		// ClassReloadingStrategy.fromInstalledAgent());
		//
		// Foo foo = new Foo();
		// System.out.println(foo.m());
		// TypePool typePool = TypePool.Default.ofClassPath();
		// new
		// ByteBuddy().redefine(typePool.describe("com.dtstack.catcher.Catcher$Bar").resolve(),
		// // do not use 'Bar.class'
		// ClassFileLocator.ForClassLoader.ofClassPath()).defineField("qux",
		// String.class) // we learn more about
		// .make().load(ClassLoader.getSystemClassLoader());
		// System.out.println(Bar.class.getDeclaredField("qux"));

		new AgentBuilder.Default().type(ElementMatchers.named("com.dtstack.catcher.Catcher$Foo"))
				.transform(new AgentBuilder.Transformer() {

					@Override
					public Builder<?> transform(Builder<?> builder, TypeDescription typeDescription,
							ClassLoader classLoader, JavaModule module) {

						
//						builder.field(ElementMatchers.named("sss")).transform(new Transformer<FieldDescription>() {
//
//							@Override
//							public FieldDescription transform(TypeDescription instrumentedType,
//									FieldDescription target) {
//								return null;
//							}
//						});

						return builder.method(ElementMatchers.named("m"))
								.intercept(MethodDelegation.to(TimeInterceptor.class));
					}
				}).installOnByteBuddyAgent();

		System.out.println(Foo.m());
		System.out.println(Foo.sss);
	}

	public String testBuddy() {
		return "dsd";
	}

	static public class Foo {
		public static String sss;

		static String m() {
			return "cccc";
		}
	}

	static public class Bar {
		String m() {
			return "bar";
		}
	}
}
