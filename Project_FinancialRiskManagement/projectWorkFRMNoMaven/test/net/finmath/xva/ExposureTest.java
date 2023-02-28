/*
 * (c) Copyright Christian P. Fries, Germany. Contact: email@christian-fries.de.
 *
 * Created on 01.10.2015
 */

package net.finmath.xva;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import net.finmath.exception.CalculationException;
import net.finmath.marketdata.model.AnalyticModel;
import net.finmath.marketdata.model.AnalyticModelFromCurvesAndVols;
import net.finmath.marketdata.model.curves.Curve;
import net.finmath.marketdata.model.curves.DiscountCurve;
import net.finmath.marketdata.model.curves.DiscountCurveInterpolation;
import net.finmath.marketdata.model.curves.ForwardCurve;
import net.finmath.marketdata.model.curves.ForwardCurveInterpolation;
import net.finmath.montecarlo.interestrate.CalibrationProduct;
import net.finmath.montecarlo.interestrate.LIBORMarketModel;
import net.finmath.montecarlo.interestrate.LIBORModelMonteCarloSimulationModel;
import net.finmath.montecarlo.interestrate.LIBORMonteCarloSimulationFromLIBORModel;
import net.finmath.montecarlo.interestrate.models.LIBORMarketModelFromCovarianceModel;
import net.finmath.montecarlo.interestrate.models.LIBORMarketModelFromCovarianceModel.Measure;
import net.finmath.montecarlo.interestrate.models.covariance.LIBORCorrelationModelExponentialDecay;
import net.finmath.montecarlo.interestrate.models.covariance.LIBORCovarianceModelFromVolatilityAndCorrelation;
import net.finmath.montecarlo.interestrate.models.covariance.LIBORVolatilityModelFromGivenMatrix;
import net.finmath.montecarlo.interestrate.products.AbstractLIBORMonteCarloProduct;
import net.finmath.montecarlo.interestrate.products.Swap;
import net.finmath.montecarlo.interestrate.products.SwapLeg;
import net.finmath.montecarlo.interestrate.products.Swaption;
import net.finmath.montecarlo.interestrate.products.TermStructureMonteCarloProduct;
import net.finmath.montecarlo.interestrate.products.components.ExposureEstimator;
import net.finmath.montecarlo.interestrate.products.components.Notional;
import net.finmath.montecarlo.interestrate.products.components.NotionalFromConstant;
import net.finmath.montecarlo.interestrate.products.indices.AbstractIndex;
import net.finmath.montecarlo.interestrate.products.indices.LIBORIndex;
import net.finmath.montecarlo.process.EulerSchemeFromProcessModel;
import net.finmath.plots.Named;
import net.finmath.plots.Plot2D;
import net.finmath.plots.Point2D;
import net.finmath.stochastic.RandomVariable;
import net.finmath.stochastic.Scalar;
import net.finmath.time.RegularSchedule;
import net.finmath.time.Schedule;
import net.finmath.time.ScheduleGenerator;
import net.finmath.time.TimeDiscretization;
import net.finmath.time.TimeDiscretizationFromArray;
import net.finmath.time.businessdaycalendar.BusinessdayCalendar;
import net.finmath.time.businessdaycalendar.BusinessdayCalendarExcludingTARGETHolidays;
import net.finmath.xva.CreditSupportAnnex;
import net.finmath.xva.FactorKey;
import net.finmath.xva.FactorType;
import net.finmath.xva.Metric;
import net.finmath.xva.Surface;
import net.finmath.xva.SurfaceContainer;
import net.finmath.xva.calculation.CollateralSimulation;
import net.finmath.xva.calculation.CounterpartyExposureAggregation;
import net.finmath.xva.calculation.PostCollateralExposureSimulation;
import net.finmath.xva.cpty.Counterparty;
import net.finmath.xva.trade.SwapTrade;
import net.finmath.xva.trade.Trade;

/**
 * 
 *
 */
public class ExposureTest {

	public static void main(String[] args) throws CalculationException, EncryptedDocumentException, InvalidFormatException, IOException {

		// ----------------------------------------------------------------------------------------------------
		// Interest rate curves are imported here
		AnalyticModel model = getAnalyticModel();

		/*
		 * Create a counterparty, sarebbe intereressante creare una simulazione con più
		 * counterparty caratterizzate da diversi rating e quindi diversi livelli di
		 * threshold e mta
		 */
		Counterparty eurCorporate = new Counterparty("012345", "Mid-sized EUR corporate Ltd.", "BBB");

		// Scelta del modello di simulazione, modello con n titoli o modello con 2
		// titoli e controllo della validità dell'input
		int sceltaMercato = -1;
		while (true) {
			System.out.println("Effettuare simulazione con N strumenti creati in modo casuale o con strumenti creati per evidenziare l'effetto netting?");
			System.out.println("0: simulazione con N strumenti, 1: simulazione per evidenziare il netting");

			JFrame jf = new JFrame();
			jf.setAlwaysOnTop(true);
			Scanner input = new Scanner(System.in);
			sceltaMercato = input.nextInt();

			// con questa condizione impedisco al programma di continuare finché
			// sceltaMercato non è un valore valido
			if (sceltaMercato == 0 || sceltaMercato == 1) {
				break;
			}

			// Alert
			int response = JOptionPane.showConfirmDialog(jf,
					"Il numero inserito non è valido! Chiudere questa finestra e inserire un numero valido.",
					"WARNING!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		}

		// creo vettore dei trade
		Trade[] tradesWithCpty = null;

		// se sceltaMercato = 0 costruisco mercato reale, se sceltaMercato = 1 simulo
		// mercato con massimo 2 titoli
		int numeroTitoli = -1;

		if (sceltaMercato == 0) {
			// mercato con n titoli

			// numero massimo titoli simulabili, più titoli immetto, più tempo ci impiegherà
			// la simulazione
			int max = 200;

			// Inserimento numero di swap con controllo di validità dell'input
			while (true) {
				JFrame jf = new JFrame();
				jf.setAlwaysOnTop(true);
				System.out.println("Inserire il numero di swap da simulare, massimo " + max + ".");
				Scanner input = new Scanner(System.in);
				numeroTitoli = input.nextInt();

				if (numeroTitoli <= max) {
					System.out.println("Attendere qualche secondo... Simulazione in corso");
					break;
				}

				// Alert
				int response = JOptionPane.showConfirmDialog(jf,
						"Il numero inserito non è valido! Chiudere questa finestra e inserire un numero valido.",
						"WARNING!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

			}

			// popolo il vettore dei trade con swap creati in modo casuale
			tradesWithCpty = new Trade[numeroTitoli];
			for (int i = 0; i < numeroTitoli; i++) {
				tradesWithCpty[i] = creaNuovoSwapTradeRandom(eurCorporate);
			}
		} else {
			// mercato con massimo 2 titoli

			while (true) {
				JFrame jf = new JFrame();
				jf.setAlwaysOnTop(true);
				System.out.println("Inserire numero di titoli: 1 (portafoglio senza netting), 2 (portafoglio con netting)");
				Scanner input = new Scanner(System.in);
				numeroTitoli = input.nextInt();

				if (numeroTitoli == 1 || numeroTitoli == 2) {
					System.out.println("Attendere qualche secondo... Simulazione in corso");
					break;
				}

				// Alert
				int response = JOptionPane.showConfirmDialog(jf,
						"Il numero inserito non è valido! Chiudere questa finestra e inserire un numero valido.",
						"WARNING!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			}

			/*
			 * Popolo in vettore dei trade con swap creati in modo da evidenziare il
			 * netting, nella prima gamba dell'if si crea il vettore con un singolo titolo,
			 * nell'altra gamba si crea il vettore con lo stesso strumento ed un altro
			 * creato appositamente per avere comportamento opposto al primo
			 */
			if (numeroTitoli == 1) {
				tradesWithCpty = new Trade[1];
				tradesWithCpty[0] = creaNuovoSwapTradeFisso(eurCorporate, 1);
			} else {
				tradesWithCpty = new Trade[2];
				tradesWithCpty[0] = creaNuovoSwapTradeFisso(eurCorporate, 1);
				tradesWithCpty[1] = creaNuovoSwapTradeFisso(eurCorporate, 2);
			}

		}

		// ----------------------------------------------------------------------------------------------------

		// This is the Monte Carlo simulation model we use for the simulation.
		LIBORModelMonteCarloSimulationModel lmm = createLIBORMarketModel(Measure.SPOT, 10000, 5, 0.1);

		// ----------------------------------------------------------------------------------------------------
		// Generate the mark to market cube now.

		SurfaceContainer markToMarketCube = new SurfaceContainer(lmm.getTimeDiscretization(), "discountCurve");
		/*
		 * For every trade in the portfolio...
		 */
		for (Trade trade : tradesWithCpty) {
			AbstractLIBORMonteCarloProduct ithProduct = (AbstractLIBORMonteCarloProduct) trade.getProduct();

			// Initialize the exposure estimator for the i-th product in the portfolio
			TermStructureMonteCarloProduct swapExposureEstimator = new ExposureEstimator(ithProduct);

			// Initialize a single "slice" of the cube
			RandomVariable[] tradeExposureSimulation = new RandomVariable[lmm.getTimeDiscretization().getNumberOfTimes()];

			/*
			 * ... and for every point in the time discretization, evaluate the exposure,
			 */
			for (int timeIndex = 0; timeIndex < lmm.getTimeDiscretization().getNumberOfTimes(); timeIndex++) {
				double observationTime = lmm.getTimeDiscretization().getTime(timeIndex);

				tradeExposureSimulation[timeIndex] = swapExposureEstimator.getValue(observationTime, lmm); 
																											
			}

			/*
			 * Put the simulated exposure in a surface.
			 */
			Surface ithTrade = new Surface(trade.getTradeId(), FactorType.Trade, Metric.Value, "discountCurve",
					tradeExposureSimulation, lmm.getTimeDiscretization());

			// Add the surface to the cube.
			markToMarketCube = markToMarketCube.addSurface(ithTrade);
		}

		// Definisco parametri del CSA
		double TRec = 50000.0;
		double TPost = 50000.0;
		double MTARec = 10000.0;
		double MTAPost = 10000.0;

		// Create a credit support annex.
		CreditSupportAnnex CSA = new CreditSupportAnnex(TRec, TPost, MTARec, MTAPost);

		// This object aggregates the exposure in the mark to market cube over different
		// sub-portfolios. V = \sum V_i
		CounterpartyExposureAggregation CEA = new CounterpartyExposureAggregation(model, "CEAtestID", markToMarketCube,	CSA, eurCorporate, tradesWithCpty);

		// Simulate the collateral account C
		CollateralSimulation CS = new CollateralSimulation(model, "CStestID", markToMarketCube, CSA, eurCorporate, tradesWithCpty);

		// Compute the exposure after the collateral V - C
		PostCollateralExposureSimulation PCES = new PostCollateralExposureSimulation(model, "PCEStestID", markToMarketCube, CSA, eurCorporate, tradesWithCpty);

		// Effettuo simulazioni
		CEA.calculate();
		CS.calculate();
		PCES.calculate();

		// Creo matrice per contenere le 2 valutazioni e confronto CEA e PCES (metodo compare)
		// Creo matrice per salvare i dati della simulazione, 120 righe e 2 colonne che rappresenteranno V e V-C
		double[][] returnMatrix = new double[lmm.getTimeDiscretization().getNumberOfTimes()][2];
		returnMatrix = compare(CEA, PCES, returnMatrix);

		// Crea excel rappresentante i grafici
		creaGrafico(returnMatrix, numeroTitoli);
	}

	/*
	 * This method returns a Monte Carlo simulation of the Libor Market Model Take
	 * this as given: DO NOT TOUCH IT :-)
	 */
	public static LIBORModelMonteCarloSimulationModel createLIBORMarketModel(Measure measure, int numberOfPaths,
			int numberOfFactors, double correlationDecayParam) throws CalculationException {

		/*
		 * Create the libor tenor structure and the initial values
		 */
		double liborPeriodLength = 0.25;
		double liborRateTimeHorzion = 20.0;
		TimeDiscretizationFromArray liborPeriodDiscretization = new TimeDiscretizationFromArray(0.0, (int) (liborRateTimeHorzion / liborPeriodLength), liborPeriodLength);

		AnalyticModel model = getAnalyticModel();

		ForwardCurve forwardCurveInterpolation = model.getForwardCurve("forwardCurve");
		DiscountCurve discountCurveInterpolation = model.getDiscountCurve("discountCurve");

		/*
		 * Create a simulation time discretization
		 */
		double lastTime = 15.0;
		double dt = 0.125;

		TimeDiscretizationFromArray timeDiscretizationFromArray = new TimeDiscretizationFromArray(0.0,
				(int) (lastTime / dt), dt);

		/*
		 * Create a volatility structure v[i][j] = sigma_j(t_i)
		 */
		double[][] volatility = new double[timeDiscretizationFromArray.getNumberOfTimeSteps()][liborPeriodDiscretization.getNumberOfTimeSteps()];
		for (int timeIndex = 0; timeIndex < volatility.length; timeIndex++) {
			for (int liborIndex = 0; liborIndex < volatility[timeIndex].length; liborIndex++) {
				// Create a very simple volatility model here
				double time = timeDiscretizationFromArray.getTime(timeIndex);
				double maturity = liborPeriodDiscretization.getTime(liborIndex);
				double timeToMaturity = maturity - time;

				double instVolatility;
				if (timeToMaturity <= 0) {
					instVolatility = 0; // This forward rate is already fixed, no volatility
				} else {
					instVolatility = 0.3 + 0.2 * Math.exp(-0.25 * timeToMaturity);
				}

				// Store
				volatility[timeIndex][liborIndex] = instVolatility;
			}
		}
		LIBORVolatilityModelFromGivenMatrix volatilityModel = new LIBORVolatilityModelFromGivenMatrix(
				timeDiscretizationFromArray, liborPeriodDiscretization, volatility);

		/*
		 * Create a correlation model rho_{i,j} = exp(-a * abs(T_i-T_j))
		 */
		LIBORCorrelationModelExponentialDecay correlationModel = new LIBORCorrelationModelExponentialDecay(
				timeDiscretizationFromArray, liborPeriodDiscretization, numberOfFactors, correlationDecayParam);

		/*
		 * Combine volatility model and correlation model to a covariance model
		 */
		LIBORCovarianceModelFromVolatilityAndCorrelation covarianceModel = new LIBORCovarianceModelFromVolatilityAndCorrelation(
				timeDiscretizationFromArray, liborPeriodDiscretization, volatilityModel, correlationModel);

		// BlendedLocalVolatlityModel (future extension)
		// AbstractLIBORCovarianceModel covarianceModel2 = new
		// BlendedLocalVolatlityModel(covarianceModel, 0.00, false);

		// Set model properties
		Map<String, String> properties = new HashMap<String, String>();

		// Choose the simulation measure
		properties.put("measure", measure.name());

		// Choose log normal model
		properties.put("stateSpace", LIBORMarketModelFromCovarianceModel.StateSpace.LOGNORMAL.name());

		// Empty array of calibration items - hence, model will use given covariance
		CalibrationProduct[] calibrationItems = new CalibrationProduct[0];

		/*
		 * Create corresponding LIBOR Market Model
		 */
		@SuppressWarnings("deprecation")
		LIBORMarketModel liborMarketModel = new LIBORMarketModelFromCovarianceModel(liborPeriodDiscretization,
				forwardCurveInterpolation, discountCurveInterpolation, covarianceModel, calibrationItems, properties);

		final EulerSchemeFromProcessModel process = new EulerSchemeFromProcessModel(
				liborMarketModel, new net.finmath.montecarlo.BrownianMotionLazyInit(timeDiscretizationFromArray,
						numberOfFactors, numberOfPaths, 3141 /* seed */),
				EulerSchemeFromProcessModel.Scheme.PREDICTOR_CORRECTOR);

		return new LIBORMonteCarloSimulationFromLIBORModel(liborMarketModel, process);
	}

	public static AnalyticModel getAnalyticModel() {
		double liborPeriodLength = 0.25;
		// Create the forward curve (initial value of the LIBOR market model)
		ForwardCurveInterpolation forwardCurveInterpolation = ForwardCurveInterpolation.createForwardCurveFromForwards(
				"forwardCurve" /* name of the curve */,
				new double[] { 0.0, 1.0, 2.0, 5.0, 40.0 } /* fixings of the forward */,
				new double[] { 0.025, 0.025, 0.025, 0.025, 0.025 } /* forwards */,
				liborPeriodLength /* tenor / period length */
		);

		// Create the discount curve
		DiscountCurveInterpolation discountCurveInterpolation = DiscountCurveInterpolation
				.createDiscountCurveFromZeroRates("discountCurve" /* name of the curve */,
						new double[] { 0.5, 1.0, 2.0, 5.0, 40.0 } /* maturities */,
						new double[] { 0.02, 0.02, 0.02, 0.02, 0.03 } /* zero rates */
				);

		return new AnalyticModelFromCurvesAndVols(
				new Curve[] { discountCurveInterpolation, forwardCurveInterpolation });
	}

	// Si confrontano le due esposizioni e viene restituita una matrice contenente i risultati del confronto 
	public static double[][] compare(CounterpartyExposureAggregation CEA, PostCollateralExposureSimulation PCES, double[][] returnMatrix) {
		TimeDiscretization timeDiscretization = CEA.getSurfaceContainer().getTimeDiscretization();
		int tempoTotale = timeDiscretization.getNumberOfTimes();
		double quantile = 0.95;

		// Si definiscono le factorkey per identificare le surface
		FactorKey factorV = new FactorKey("sommaSurface", FactorType.Cpty, Metric.Value);
		FactorKey factorVsubC = new FactorKey("Esposizione", FactorType.Cpty, Metric.Exposure);

		// Si estraggono le surface da confrontare
		Surface V = CEA.getSurface(factorV);
		Surface VsubC = PCES.getSurface(factorVsubC);

		// Si definiscono i dataframe delle 2 surface
		RandomVariable[] dataFrameV = V.getDataFrame();
		RandomVariable[] dataFrameVsubC = VsubC.getDataFrame();

		/*
		 * Nel metodo si passa la matrice returnMatrix che si andrà a popolare
		 * direttamente con i risultati del metodo getQuantile() richiamato prima
		 * sull'esposizione e poi sull'esposizione collateralizzata
		 */

		for (int time = 0; time < tempoTotale; time++) {
			returnMatrix[time][0] = dataFrameV[time].getQuantile(quantile);
			returnMatrix[time][1] = dataFrameVsubC[time].getQuantile(quantile);
		}

		return returnMatrix;
	}

	// Il metodo stampa nelle prime due colonne del file grafico.xlsx i valori ottenuti dal metodo compare(). 
	// Questi dati verranno utilizzati per generare il grafico. 
	public static void creaGrafico(double[][] matrix, int numeroStrumenti) throws InvalidFormatException, EncryptedDocumentException, IOException {

		// Creazione del file e scrittura manuale della prima riga che conterrà le
		// denominazioni "V", "V-C" e numero dei titoli simulati
		File file = new File("grafico.xlsx");
		FileInputStream f = new FileInputStream(file);
		// Si richiama l’oggetto Workbook di tipo XSSF per poter scrivere i dati risultanti dalla 
		// simulazione in un file Excel con estensione .xls e si richiama il foglio di lavoro
		Workbook wb = WorkbookFactory.create(f); 
		Sheet sheet = wb.getSheetAt(0);
		// Si crea la riga 
		int rowCount = 0;
		// Si crea una riga alla posizione 0
		Row row = sheet.createRow(0);
		// Si impostano i titoli delle colonne 
		Cell c = row.createCell(0);
		c.setCellValue("V");
		c = row.createCell(1);
		c.setCellValue("V-C");
		c = row.createCell(2);
		c.setCellValue("# strumenti: " + numeroStrumenti);

		// Ciclo per scrivere nel file i valori ricavati dalla simulazione
		for (int i = 0; i < matrix.length; i++) {
			rowCount++;
			row = sheet.createRow(rowCount);
			for (int j = 0; j < matrix[0].length; j++) {
				c = row.createCell(j);
				c.setCellValue(matrix[i][j]);
			}
		}

		int aprireFile = -1;

		// Procedura per aprire il file grafico.xlsx 
		while (true) {

			// Richiesta input da tastiera per apertura del file  
			JFrame jf = new JFrame();
			jf.setAlwaysOnTop(true);
			System.out.println("Aprire il file excel? 1 = apri il file excel, 0 = non aprire il file excel");
			System.out.println("(Al fine di evitare errori, se si vuole aprire il fine, esso non deve essere già aperto)");
			Scanner input = new Scanner(System.in);
			aprireFile = input.nextInt();

			if (aprireFile == 1 || aprireFile == 0) {
				break;
			}

			// Popup per input invalido
			int response = JOptionPane.showConfirmDialog(jf,
					"Il numero inserito non è valido! Chiudere questa finestra e inserire un numero valido.",
					"WARNING!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		}

		// Arrivato a questo punto aprireFile vale 0 o 1
		if (aprireFile == 1) {
			// Se il file esiste e non è già aperto lo apre. Se è già aperto lancia l'eccezione
			try {
				Desktop desktop = Desktop.getDesktop();
				if (!Desktop.isDesktopSupported()) {
					System.out.println("not supported");
					return;
				}
				if (file.exists()) {
					FileOutputStream outputStream = new FileOutputStream("grafico.xlsx");
					wb.write(outputStream);
					wb.close();
					outputStream.close();
					desktop.open(file);
				}
			} catch (FileNotFoundException e) {
				JFrame jf = new JFrame();
				jf.setAlwaysOnTop(true);
				int response = JOptionPane.showConfirmDialog(jf, "Chiudere prima le altre istanze del file excel!",
						"WARNING!", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
			}
		} else {
			// non aprire file
			return;
		}
	}

	// metodo per creare un nuovo swap trade random
	public static SwapTrade creaNuovoSwapTradeRandom(Counterparty eurCorporate) {
		Schedule legScheduleRec = ScheduleGenerator.createScheduleFromConventions(
				LocalDate.of(2015, Month.JANUARY, 03) /* referenceDate */,
				LocalDate.of(2015, Month.JANUARY, 06) /* startDate */,
				LocalDate.of(2025, Month.JANUARY, 06) /* maturityDate */,
				ScheduleGenerator.Frequency.ANNUAL /* frequency */,
				ScheduleGenerator.DaycountConvention.ACT_365 /* daycountConvention */,
				ScheduleGenerator.ShortPeriodConvention.FIRST /* shortPeriodConvention */,
				BusinessdayCalendar.DateRollConvention.FOLLOWING /* dateRollConvention */,
				new BusinessdayCalendarExcludingTARGETHolidays() /* businessdayCalendar */, 0 /* fixingOffsetDays */,
				0 /* paymentOffsetDays */);

		Schedule legSchedulePay = ScheduleGenerator.createScheduleFromConventions(
				LocalDate.of(2015, Month.JANUARY, 03) /* referenceDate */,
				LocalDate.of(2015, Month.JANUARY, 06) /* startDate */,
				LocalDate.of(2025, Month.JANUARY, 06) /* maturityDate */,
				ScheduleGenerator.Frequency.QUARTERLY /* frequency */,
				ScheduleGenerator.DaycountConvention.ACT_365 /* daycountConvention */,
				ScheduleGenerator.ShortPeriodConvention.FIRST /* shortPeriodConvention */,
				BusinessdayCalendar.DateRollConvention.FOLLOWING /* dateRollConvention */,
				new BusinessdayCalendarExcludingTARGETHolidays() /* businessdayCalendar */, 0 /* fixingOffsetDays */,
				0 /* paymentOffsetDays */);

		// creo swap con nozionale compreso tra (upper euro) e (lower euro)
		int upper = 1000000;
		int lower = 100000;
		double nozionaleRandom = Math.random() * (upper - lower) + lower;
		Notional notional = new NotionalFromConstant(nozionaleRandom);
		AbstractIndex index = new LIBORIndex("forwardCurve", 0.0, 0.25);
		double fixedCoupon = 0.025;

		// creo swap di direzioni inverse in modo randomico (p = 0.5) di entrare in una
		// gamba o nell'altra
		Swap newSwap = null;
		if (Math.random() < 0.5) {
			SwapLeg newSwapLegRec = new SwapLeg(legSchedulePay, notional, index, 0.0 /* spread */, false /* isNotionalExchanged */);
			SwapLeg newSwapLegPay = new SwapLeg(legScheduleRec, notional, null, fixedCoupon /* spread */, false /* isNotionalExchanged */);
			newSwap = new Swap(newSwapLegRec, newSwapLegPay);
		} else {
			SwapLeg newSwapLegRec = new SwapLeg(legScheduleRec, notional, null, fixedCoupon /* spread */, false /* isNotionalExchanged */);
			SwapLeg newSwapLegPay = new SwapLeg(legSchedulePay, notional, index, 0.0 /* spread */, false /* isNotionalExchanged */);
			newSwap = new Swap(newSwapLegRec, newSwapLegPay);
		}

		// creo tradeid con numeri casuali per non avere ripetizioni
		String tradeId = "";
		for (int i = 0; i < 7; i++) {
			int numero = (int) (Math.random() * 9);
			tradeId = tradeId.concat(Integer.toString(numero));
		}

		// creo nettingSetId e marginSetId con numeri casuali
		String nettingSetId = "";
		String marginSetId = "";
		for (int i = 0; i < 6; i++) {
			int numero = (int) (Math.random() * 9);
			nettingSetId = nettingSetId.concat(Integer.toString(numero));
			marginSetId = marginSetId.concat(Integer.toString(numero));
		}
		// parte costante dell'Id
		nettingSetId = nettingSetId.concat("_NS");
		marginSetId = marginSetId.concat("_NS");
		// parte finale dell'Id
		int numero = (int) (Math.random() * 9);
		nettingSetId = nettingSetId.concat(Integer.toString(numero));
		marginSetId = marginSetId.concat(Integer.toString(numero));

		/*
		 * Se si volesse avere la certezza che questi valori non coincidano mai
		 * bisognerebbe eseguire un controllo ma la possibilità che questo accada è
		 * infinitesima, in un ambiente lavorativo andrebbe comunque implementato questo
		 * controllo
		 */

		return new SwapTrade(tradeId, eurCorporate, nettingSetId, marginSetId, "EUR", newSwap);
	}

	public static SwapTrade creaNuovoSwapTradeFisso(Counterparty c, int i) {
		// Creo i 2 swap in ogni caso e poi vedo quale ritornare
		Schedule legScheduleRec = ScheduleGenerator.createScheduleFromConventions(
				LocalDate.of(2015, Month.JANUARY, 03) /* referenceDate */,
				LocalDate.of(2015, Month.JANUARY, 06) /* startDate */,
				LocalDate.of(2025, Month.JANUARY, 06) /* maturityDate */,
				ScheduleGenerator.Frequency.ANNUAL /* frequency */,
				ScheduleGenerator.DaycountConvention.ACT_365 /* daycountConvention */,
				ScheduleGenerator.ShortPeriodConvention.FIRST /* shortPeriodConvention */,
				BusinessdayCalendar.DateRollConvention.FOLLOWING /* dateRollConvention */,
				new BusinessdayCalendarExcludingTARGETHolidays() /* businessdayCalendar */, 0 /* fixingOffsetDays */,
				0 /* paymentOffsetDays */);

		Schedule legSchedulePay = ScheduleGenerator.createScheduleFromConventions(
				LocalDate.of(2015, Month.JANUARY, 03) /* referenceDate */,
				LocalDate.of(2015, Month.JANUARY, 06) /* startDate */,
				LocalDate.of(2025, Month.JANUARY, 06) /* maturityDate */,
				ScheduleGenerator.Frequency.QUARTERLY /* frequency */,
				ScheduleGenerator.DaycountConvention.ACT_365 /* daycountConvention */,
				ScheduleGenerator.ShortPeriodConvention.FIRST /* shortPeriodConvention */,
				BusinessdayCalendar.DateRollConvention.FOLLOWING /* dateRollConvention */,
				new BusinessdayCalendarExcludingTARGETHolidays() /* businessdayCalendar */, 0 /* fixingOffsetDays */,
				0 /* paymentOffsetDays */);
		Notional notional1 = new NotionalFromConstant(1000000.0);
		Notional notional2 = new NotionalFromConstant(500000.0);
		AbstractIndex index = new LIBORIndex("forwardCurve", 0.0, 0.25);
		double fixedCoupon = 0.025;

		// Swap1 (dato dal progetto iniziale)
		SwapLeg swapLegRec1 = new SwapLeg(legScheduleRec, notional1, null, fixedCoupon /* spread */, false /* isNotionalExchanged */);
		SwapLeg swapLegPay1 = new SwapLeg(legSchedulePay, notional1, index, 0.0 /* spread */, false /* isNotionalExchanged */);
		// Create an interest rate swap
		Swap swap1 = new Swap(swapLegRec1, swapLegPay1);

		// Swap2 creato per evidenziare il netting, creato con metà del nozionale e
		// invertendo le gambe dello swap
		SwapLeg swapLegRec2 = new SwapLeg(legSchedulePay, notional2, index, 0.0 /* spread */, false /* isNotionalExchanged */);
		SwapLeg swapLegPay2 = new SwapLeg(legScheduleRec, notional2, null, fixedCoupon /* spread */, false /* isNotionalExchanged */);
		// Create an interest rate swap, inverto le gambe
		Swap swap2 = new Swap(swapLegRec2, swapLegPay2);

		SwapTrade swapTrade1 = new SwapTrade("0123456", c, "012345_NS1", "012345_MS1", "EUR", swap1);
		SwapTrade swapTrade2 = new SwapTrade("0123457", c, "012345_NS2", "012345_MS2", "EUR", swap2);

		// i rappresenta la scelta fatta dall'utente del numero di titoli 
		if (i == 1) {
			return swapTrade1;
		} else {
			return swapTrade2;
		}

	}

}
