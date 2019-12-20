package neuralnet;
public class TrainingData {
 
    float[] data;
    float[] expectedOutput;
   
    public TrainingData(float[] data, float[] expectedOutput) {
        this.data = data;
        this.expectedOutput = expectedOutput;
    }
   
    
    public static void main(String[] args) {
		int pre = 129;
		int next = 16;
		float wpern = (float)pre /next;
		System.out.println(wpern);
	}
}