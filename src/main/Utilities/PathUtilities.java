package main.Utilities;

public class PathUtilities {

    public static String getProjectPath(){
        PathUtilities obj = new PathUtilities();
        String path = obj.getClass().getClassLoader().getResource("").getPath();
        int indexOfLastSlash=path.substring(0,path.length()-1).lastIndexOf("/");
        String projectPath=path.substring(0,indexOfLastSlash+1);
        return projectPath;
    }

}
