package com.hackhalo2.rendering.obj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.nio.FloatBuffer;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.util.vector.Vector3f;

import com.hackhalo2.rendering.model.Face;
import com.hackhalo2.rendering.model.Model;
import com.hackhalo2.rendering.model.ModelUtils;
import com.hackhalo2.rendering.util.VBOContainer;
import com.hackhalo2.rendering.util.VBOContainer.ContainerType;

/*
 * This class is loosely based on Oskar Veerhoek's implementation.
 * It includes some optimizations and removes unneeded functions that the RenderEngine doesn't support
 * (i.e. DisplayLists).
 * 
 * Oskar's Implementation can be found at:
 * https://github.com/OskarVeerhoek/YouTube-tutorials/blob/master/src/utility/OBJLoader.java
 */
public class OBJHandler {

	//Prevent Initialization
	private OBJHandler() { }
	
	public static Set<VBOContainer> generateVBOSets(Model model, int vertexHandle, int normalHandle) {
		//Create the Buffers
		FloatBuffer vertices = BufferUtils.createFloatBuffer(model.bufferSize());
		FloatBuffer normals = BufferUtils.createFloatBuffer(model.bufferSize());
		
		//Fill the Buffers
		for(Face f : model.getFaces()) {
			vertices.put(ModelUtils.asFloats(model.getVertex((int)f.getVertex().x-1)));
			vertices.put(ModelUtils.asFloats(model.getVertex((int)f.getVertex().y-1)));
			vertices.put(ModelUtils.asFloats(model.getVertex((int)f.getVertex().z-1)));
			
			normals.put(ModelUtils.asFloats(model.getNormal((int)f.getNormal().x-1)));
			normals.put(ModelUtils.asFloats(model.getNormal((int)f.getNormal().y-1)));
			normals.put(ModelUtils.asFloats(model.getNormal((int)f.getNormal().z-1)));
		}
		
		//Flip the buffers
		vertices.flip();
		normals.flip();
		
		//Define the buffers
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, vertices, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, normalHandle);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, normals, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		//Throw the Containers in a Set and return them
		Set<VBOContainer> set = new HashSet<VBOContainer>();
		
		set.add(new VBOContainer(ContainerType.VERTEX, vertexHandle, 3, 0));
		set.add(new VBOContainer(ContainerType.NORMAL, normalHandle, 3, 0));
		
		return set;
	}
	
	public static Model loadModel(URI file) throws FileNotFoundException, IOException {
		return loadModel(new File(file));
	}

	public static Model loadModel(File file) throws FileNotFoundException, IOException {
		//Check to make sure the file is an obj file
		if(!(file.toString().toLowerCase().endsWith(".obj"))) throw new IOException("File was not an OBJ file");

		BufferedReader input = new BufferedReader(new FileReader(file));
		Model model = new Model();
		String l;
		
		//Read the File
		while((l = input.readLine()) != null) {
			if(l.startsWith("#")) continue; //Skip comments

			//The other important stuff
			else if(l.startsWith("v ")) { //Vertices
				String[] split = l.split(" "); //Split the spaces
				float x = Float.valueOf(split[1]); //Get the X coord
				float y = Float.valueOf(split[2]); //Get the Y coord
				float z = Float.valueOf(split[3]); //Get the Z coord
				model.addVertex(new Vector3f(x, y, z));
			} else if(l.startsWith("vn ")) { //Vertex Normals
				String[] split = l.split(" "); //Split the spaces
				float x = Float.valueOf(split[1]); //Get the X coord
				float y = Float.valueOf(split[2]); //Get the Y coord
				float z = Float.valueOf(split[3]); //Get the Z coord
				model.addNormal(new Vector3f(x, y, z));
			} else if(l.startsWith("f ")) { //Faces
				//Magic is done here
				Vector3f vertexIndices = new Vector3f(Float.valueOf(l.split(" ")[1].split("/")[0]),
						Float.valueOf(l.split(" ")[2].split("/")[0]),
						Float.valueOf(l.split(" ")[3].split("/")[0]));
				Vector3f normalIndices = new Vector3f(Float.valueOf(l.split(" ")[1].split("/")[2]),
						Float.valueOf(l.split(" ")[2].split("/")[2]),
						Float.valueOf(l.split(" ")[3].split("/")[2]));
				model.addFace(new Face(vertexIndices, normalIndices));
			} else if(l.startsWith("o ")) {
				String name = l.replaceFirst("o ", ""); //Strip out the beginning identifier
				model.setName(name);
			}
		}

		input.close();
		model.finalize(); //Finalize the object to prevent people fucking with it
		return model;
	}

}
