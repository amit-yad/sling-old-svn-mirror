/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.sling.slingclipse.http.impl;

import java.io.File;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.sling.slingclipse.api.FileInfo;

public class RepositoryImpl extends AbstractRepository{
	
	private final HttpClient httpClient = new HttpClient();	

	@Override
	public void addNode(FileInfo fileInfo) {
		PostMethod post = new PostMethod(repositoryInfo.getUrl()+fileInfo.getRelativeLocation());
		try{
			File f=new File(fileInfo.getLocation());
 			Part[] parts ={ new FilePart(fileInfo.getName(), f)};
			post.setRequestEntity(new MultipartRequestEntity(parts,post.getParams()));
			httpClient.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(repositoryInfo.getUsername(),repositoryInfo.getPassword()));
			httpClient.getParams().setAuthenticationPreemptive(true);
			int responseStatus=httpClient.executeMethod(post);
			//TODO handle the response status
		} catch(Exception e){
			//TODO handle the error
		}finally{
			post.releaseConnection();
		}
	}

	@Override
	public void deleteNode(FileInfo fileInfo) {
		PostMethod post = new PostMethod(repositoryInfo.getUrl()+fileInfo.getRelativeLocation()+"/"+fileInfo.getName());
		try{
			Part[] parts ={new StringPart(":operation", "delete")};
			post.setRequestEntity(new MultipartRequestEntity(parts,post.getParams()));
			httpClient.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(repositoryInfo.getUsername(),repositoryInfo.getPassword()));
			httpClient.getParams().setAuthenticationPreemptive(true);
			int responseStatus=httpClient.executeMethod(post);
			//TODO handle the response status
		} catch(Exception e){
			//TODO handle the error
		}finally{
			post.releaseConnection();
		}
		
	}

}
