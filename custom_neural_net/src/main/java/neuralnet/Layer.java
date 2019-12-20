package neuralnet;

public class Layer {
	public Neuron[] neurons;
	
	// Constructor for the hidden and output layer
	public Layer(int inNeurons,int numberNeurons) {
		this.neurons = new Neuron[numberNeurons];
		
		for(int i = 0; i < numberNeurons; i++) {
			float[] weights = new float[inNeurons];
			for(int j = 0; j < inNeurons; j++) {
				weights[j] = StatUtil.RandomFloat(Neuron.minWeightValue, Neuron.maxWeightValue);
			}
			neurons[i] = new Neuron(weights,StatUtil.RandomFloat(0, 1));
		}
	}
	
	
	// Constructor for the input layer
	public Layer(float input[]) {
		this.neurons = new Neuron[input.length];
		for(int i = 0; i < input.length; i++) {
			this.neurons[i] = new Neuron(input[i]);
		}
	}
	
	int getLargestNeuronIndex(){
		int result = 0;

        for (int i = 0; i < neurons.length; i++) {
            if (neurons[i].value > neurons[result].value)
                result = i;
        }

        return result;
	}
	
	float[] getNeuronValues(){
		float[] result = new float[neurons.length];;

        for (int i = 0; i < neurons.length; i++) {
        	result[i] = neurons[i].value;
        }
        return result;
	}
	
	void printNeuronValues(){
		float[] result = new float[neurons.length];;
		System.out.print("[ ");
        for (int i = 0; i < neurons.length; i++) {
        	System.out.print(neurons[i].value+",");
        }
        System.out.print(" ]");
        System.out.println("");
	}
}
