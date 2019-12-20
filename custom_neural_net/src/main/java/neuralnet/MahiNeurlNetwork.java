package neuralnet;

import java.io.FileWriter;

import mnist.ArrayUtil;
import mnist.MNIST;
import mnist.MNISTReader;

public class MahiNeurlNetwork {
	// Variable Declaration

	// mnist reader
	static MNISTReader reader;

	// Layers
	static Layer[] layers; // My changes
    static int numberOfLayers = 5;
	// Training data
	static TrainingData[] tDataSet; // My changes

	// Main Method
	public static void main(String[] args) {
		// My changes
		// Set the Min and Max weight value for all Neurons
		Neuron.setRangeWeight(-1, 1);

		// Create the layers
		// Notes: One thing you didn't code right is that neurons in a layer
		// need to have number of weights corresponding to the previous layer
		// which means that the first hidden layer need to have 2 weights per neuron and
		// 6 neurons

		layers = new Layer[numberOfLayers];
		layers[0] = null; // Input Layer 0,2
		layers[1] = new Layer(784, 16); // Hidden Layer 784,16
		layers[2] = new Layer(16, 10); // Output Layer 16,16
		layers[3] = new Layer(10, 16); // Output Layer 16,10
		layers[4] = new Layer(16, 784);
		// Create the training data
		CreateTrainingData();

		System.out.println("============");
		System.out.println("Output before training");
		System.out.println("============");
		for (int i = 0; i < 3; i++) {
			System.out.println("input provided : "
					+ ArrayUtil.indexOfLargest(reader.getData().get(i).getLabels().toFloatVector()));
			int pridiction = pridict(reader.getData().get(i).getFeatures().toFloatVector());
			System.out.println("model output : " + pridiction);
		}
          trainAutoEncoder(5, 0.05f);
		// train(5, 0.05f);
		//forwardAndTrain(4);
		System.out.println("============");
		System.out.println("Output after training");
		System.out.println("============");
		for (int i = 0; i < 20; i++) {
			System.out.println("input provided : "
					+ ArrayUtil.indexOfLargest(reader.getData().get(i).getLabels().toFloatVector()));
			MNIST.dumpMNISTDigit(reader.getData().get(i).getFeatures());
			forward(reader.getData().get(i).getFeatures().toFloatVector());
			//int pridiction = pridict(reader.getData().get(i).getFeatures().toFloatVector());
			//System.out.println("model output : " + pridiction);
			layers[4].printNeuronValues();
		}
	}

	public static float[] getOutPutLayer(float[] input) {
		forward(input);
		return layers[numberOfLayers-1].getNeuronValues();
	}

	public static int pridict(float[] input) {
		float[] lastLayerVlues = getOutPutLayer(input);
		int result = 0;
		for (int i = 0; i < lastLayerVlues.length; i++) {
			if (lastLayerVlues[i] > lastLayerVlues[result])
				result = i;
		}
		return result;
	}

	public static void saveModel(String location) {
		// FileWriter writer = new FileWriter(location);

	}

	public static void CreateTrainingData() {
		String path = "C:\\Users\\parihama\\Desktop\\learning\\java\\lib\\mnist";
		boolean training = true;
		reader = MNIST.loadMNIST(path, training);
		// MNIST.displayFirstMNIST(reader, 60000);
	}

	/*
	 * public static void createLayer(int numberOfLayers ,int numberOfNeurons) { //
	 * First bring the inputs into the input layer layers[0] layers[0] = new
	 * Layer(inputs);
	 * 
	 * for(int i = 1; i < numberOfLayers; i++) { for(int j = 0; j <
	 * layers[i].neurons.length; j++) { float sum = 0; for(int k = 0; k <
	 * layers[i-1].neurons.length; k++) { sum +=
	 * layers[i-1].neurons[k].value*layers[i].neurons[j].weights[k]; } //sum +=
	 * layers[i].neurons[j].bias; // TODO add in the bias layers[i].neurons[j].value
	 * = StatUtil.Sigmoid(sum); } } }
	 */
	public static void forward(float[] inputs) {
		// First bring the inputs into the input layer layers[0]
		layers[0] = new Layer(inputs);

		for (int i = 1; i < layers.length; i++) {
			for (int j = 0; j < layers[i].neurons.length; j++) {
				float sum = 0;
				for (int k = 0; k < layers[i - 1].neurons.length; k++) {
					sum += layers[i - 1].neurons[k].value * layers[i].neurons[j].weights[k];
				}
				// sum += layers[i].neurons[j].bias; // TODO add in the bias
				layers[i].neurons[j].value = StatUtil.Sigmoid(sum);
			}
		}
	}

	public static void forwardAndTrain(int training_iterations) {
		System.out.println("=================training started==============");
		for (int i = 0; i < training_iterations; i++) {
			System.out.println("=================epoch==============" + (i + 1));
			for (int j = 0; j < 50000; j++) {
				// System.out.println("image number---" + j);
				if (j % 1000 == 0) {
					System.out.print("-");
				}
				forwardAndWeightCahnge(reader.getData().get(j).getFeatures().toFloatVector());
			}
			System.out.println("");
		}
		System.out.println("=================training finished==============");
	}

	public static void forwardAndWeightCahnge(float[] inputs) {
		// First bring the inputs into the input layer layers[0]
		layers[0] = new Layer(inputs);
		//layers[0].printNeuronValues();
		for (int i = 1; i < layers.length; i++) {
			// for (int i = 1; i < 2; i++) {
			//System.out.println("===============layer " + i + " ===============");
			// assumption input layer has more neurons
			int weightsPerNeuron = layers[i - 1].neurons.length / layers[i].neurons.length;
			for (int j = 0; j < layers[i].neurons.length; j++) {
				float largestWeight = 0;
				// System.out.print("neuraon : "+j+" : weights");
				int devideNum = 1, numOfChangedWeights = 0;
				float sum = 0;
				for (int k = 0; k < layers[i - 1].neurons.length; k++) {

					float befor = layers[i].neurons[j].weights[k];
                    float valueToAddInWeight = (float) ((layers[i - 1].neurons[k].value - 0.5)/devideNum);
                    
					layers[i].neurons[j].weights[k] = valueToAddInWeight
							+ layers[i].neurons[j].weights[k];

					if (layers[i].neurons[j].weights[k] > largestWeight) {
						largestWeight = layers[i].neurons[j].weights[k];
					}

					if (layers[i].neurons[j].weights[k] > 0 && befor != layers[i].neurons[j].weights[k]) {
						//System.out.print("weight:" + k + "befor:" + befor + "-after:" + layers[i].neurons[j].weights[k] + ",");
						numOfChangedWeights++;
					}
					if ((weightsPerNeuron - 1) == 0 || (k > 0 && k % (weightsPerNeuron - 1) == 0)) {
						devideNum++;
					}
				}
				//System.out.println("------largestWeight---" + largestWeight);
				/*
				 * for (int k = 0; k < layers[i - 1].neurons.length; k++) {
				 * layers[i].neurons[j].weights[k] = layers[i].neurons[j].weights[k] /
				 * largestWeight; }
				 */
				//System.out.println("numOfChangedWeights--" + numOfChangedWeights);
				// sum += layers[i].neurons[j].bias; // TODO add in the bias
				layers[i].neurons[j].value = StatUtil.Sigmoid(sum);
			}
		}

	}

	// This part is heavily inspired from the website in the first note.
	// The idea is that you calculate a gradient and cache the updated weights in
	// the neurons.
	// When ALL the neurons new weight have been calculated we refresh the neurons.
	// Meaning we do the following:
	// Calculate the output layer weights, calculate the hidden layer weight then
	// update all the weights
	public static void backward(float learning_rate, float[] tData) {

		int number_layers = layers.length;
		int out_index = number_layers - 1;

		// Update the output layers
		// For each output
		for (int i = 0; i < layers[out_index].neurons.length; i++) {
			// and for each of their weights
			float output = layers[out_index].neurons[i].value;
			float target = tData[i];
			float derivative = output - target;
			float delta = derivative * (output * (1 - output));
			layers[out_index].neurons[i].gradient = delta;
			for (int j = 0; j < layers[out_index].neurons[i].weights.length; j++) {
				float previous_output = layers[out_index - 1].neurons[j].value;
				float error = delta * previous_output;
				layers[out_index].neurons[i].cache_weights[j] = layers[out_index].neurons[i].weights[j]
						- learning_rate * error;
			}
		}

		// Update all the subsequent hidden layers
		for (int i = out_index - 1; i > 0; i--) {
			// For all neurons in that layers
			for (int j = 0; j < layers[i].neurons.length; j++) {
				float output = layers[i].neurons[j].value;
				float gradient_sum = sumGradient(j, i + 1);
				float delta = (gradient_sum) * (output * (1 - output));
				layers[i].neurons[j].gradient = delta;
				// And for all their weights
				for (int k = 0; k < layers[i].neurons[j].weights.length; k++) {
					float previous_output = layers[i - 1].neurons[k].value;
					float error = delta * previous_output;
					layers[i].neurons[j].cache_weights[k] = layers[i].neurons[j].weights[k] - learning_rate * error;
				}
			}
		}

		// Here we do another pass where we update all the weights
		for (int i = 0; i < layers.length; i++) {
			for (int j = 0; j < layers[i].neurons.length; j++) {
				layers[i].neurons[j].update_weight();
			}
		}

	}

	// This function sums up all the gradient connecting a given neuron in a given
	// layer
	public static float sumGradient(int n_index, int l_index) {
		float gradient_sum = 0;
		Layer current_layer = layers[l_index];
		for (int i = 0; i < current_layer.neurons.length; i++) {
			Neuron current_neuron = current_layer.neurons[i];
			gradient_sum += current_neuron.weights[n_index] * current_neuron.gradient;
		}
		return gradient_sum;
	}

	// This function is used to train being forward and backward.
	public static void train(int training_iterations, float learning_rate) {
		System.out.println("=================training started==============");
		for (int i = 0; i < training_iterations; i++) {
			System.out.println("=================epoch==============" + (i + 1));
			for (int j = 0; j < 50000; j++) {
				if (j % 1000 == 0) {
					System.out.print("-");
				}
				forward(reader.getData().get(j).getFeatures().toFloatVector());
				// MNIST.displayMNIST(reader, j);
				backward(learning_rate, reader.getData().get(j).getLabels().toFloatVector());
			}
			System.out.println("");
		}
		System.out.println("=================training finished==============");
	}
	
	// This function is used to train being forward and backward.
		public static void trainAutoEncoder(int training_iterations, float learning_rate) {
			System.out.println("=================training started==============");
			for (int i = 0; i < training_iterations; i++) {
				System.out.println("=================epoch==============" + (i + 1));
				for (int j = 0; j < 50000; j++) {
					if (j % 1000 == 0) {
						System.out.print("-");
					}
					forward(reader.getData().get(j).getFeatures().toFloatVector());
					// MNIST.displayMNIST(reader, j);
					backward(learning_rate, reader.getData().get(j).getFeatures().toFloatVector());
				}
				System.out.println("");
			}
			System.out.println("=================training finished==============");
		}
}