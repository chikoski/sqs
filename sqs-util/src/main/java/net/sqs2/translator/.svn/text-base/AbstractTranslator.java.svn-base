/*

 AbstractTranslator.java

 Copyright 2007 KUBO Hiroya (hiroya@sfc.keio.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2007/01/28

 */
package net.sqs2.translator;

import java.io.BufferedInputStream;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.xml.transform.URIResolver;

import net.sqs2.lang.GroupThreadFactory;
import net.sqs2.util.FileUtil;

public abstract class AbstractTranslator {
	ExecutorService executor = null;
	GroupThreadFactory groupThreadFactory = null;
	Future<Boolean> translationFuture = null;

	public void translate(String sourceFile, String targetFile, URIResolver uriResolver) throws TranslatorException, IOException {
		this.translate(new File(sourceFile), new File(targetFile), uriResolver);
	}

	public InputStream translate(String sourceFile, URIResolver uriResolver) throws TranslatorException, IOException {
		return this.translate(new File(sourceFile), uriResolver);
	}

	public InputStream translate(File sourceFile, URIResolver uriResolver) throws TranslatorException, IOException {
		return this.translate(new FileInputStream(sourceFile), sourceFile.toURI().toString(), uriResolver);
	}

	public InputStream translate(InputStream sourceInputStream, String systemId, URIResolver uriResolver) throws TranslatorException, IOException {
		return this.asyncTranslate(sourceInputStream, systemId, uriResolver);
	}

	public void translate(File sourceFile, File targetFile, URIResolver uriResolver) throws TranslatorException, IOException {
		InputStream targetInputStream = this.asyncTranslate(new FileInputStream(sourceFile), sourceFile.toURI().toString(), uriResolver);
		OutputStream targetOutputStream = new FileOutputStream(targetFile);
		FileUtil.pipe(targetInputStream, targetOutputStream);
	}

	public void translate(InputStream sourceInputStream, String systemId, OutputStream targetOutputStream, URIResolver uriResolver) throws TranslatorException, IOException {
		FileUtil.pipe(this.asyncTranslate(sourceInputStream, systemId, uriResolver), targetOutputStream);
	}

	public synchronized InputStream asyncTranslate(final InputStream sourceInputStream, final String systemId, final URIResolver uriResolver) throws IOException {
		final PipedOutputStream tmpOutputStream = new PipedOutputStream();
		final PipedInputStream targetInputStream = new PipedInputStream(tmpOutputStream);

		this.groupThreadFactory = new GroupThreadFactory("net.sqs2.translator.AbstractTranslator",
				Thread.NORM_PRIORITY + 1, true);
		this.executor = Executors.newScheduledThreadPool(2, this.groupThreadFactory);
		this.translationFuture = this.executor.submit(new Callable<Boolean>() {
			public Boolean call() throws TranslatorException, IOException {
				try {
					execute(sourceInputStream, systemId, tmpOutputStream, uriResolver);
					return true;
				} catch (IOException ex) {
					ex.printStackTrace();
					throw ex;
				} catch (TranslatorException ex) {
					Logger.getLogger(getClass().getName()).warning(ex.getMessage());
					throw ex;
				} finally {
					try {
						sourceInputStream.close();
					} catch (Exception ignore) {
						ignore.printStackTrace();
					}
					try {
						tmpOutputStream.close();
					} catch (Exception ignore) {
						ignore.printStackTrace();
					}
				}
			}
		});
		return targetInputStream;
	}

	public void waitUntilDone() {
		try {
			if (this.translationFuture != null) {
				/*
				 * while(! this.translationFuture.isDone() || !
				 * this.translationFuture.isCancelled()){ Thread.yield(); }
				 */
				// Boolean result =
				// System.err.println("****");
				this.translationFuture.get();
				// System.err.println("////");
			}
			if (this.executor != null) {
				this.executor.shutdown();
			}
			if (this.groupThreadFactory != null) {
				this.groupThreadFactory.destroy();
			}

		} catch (ExecutionException ignore) {
			ignore.printStackTrace();
		} catch (InterruptedException ignore) {
			ignore.printStackTrace();
		}
	}

	public void syncTranslate(InputStream inputStream, String systemId, OutputStream outputStream, URIResolver uriResolver) throws TranslatorException, IOException {
		InputStream sourceInputStream = new BufferedInputStream(inputStream);
		OutputStream targetOutputStream = new BufferedOutputStream(outputStream);
		execute(sourceInputStream, systemId, targetOutputStream, uriResolver);
		targetOutputStream.flush();
	}
	
	public abstract void execute(InputStream sourceInputStream, String systemId, OutputStream targetOutputStream, URIResolver uriResolver)throws TranslatorException,IOException;
}
