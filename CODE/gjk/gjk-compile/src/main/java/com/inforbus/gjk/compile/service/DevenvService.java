package com.inforbus.gjk.compile.service;

import java.io.IOException;

public interface DevenvService {
	String Command(String path,String fileName,String platformType,String token) throws IOException;
}
