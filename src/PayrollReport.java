import java.io.File;
import java.util.Scanner;

public class PayrollReport {
	
	static Scanner key = new Scanner(System.in);
	static Scanner fs;
	static int opt;
	//variables for reading file
	
	static int id;
	static String fname;
	static String sname;
	static String gen;
	static double hrs,hrate;
		
	//variables for printReport method
	static double gross, deduct, net, tax, nitax, pension, otime, standard, annualgross,nirate, penrate;
		
	//variables for printWage method
	static double totalWage;
	static int count;
	
	//variables for printTotalDeductions method
	static double totaltax, totalni,totalpen;

	public static void main(String[] args) throws Exception {
		while(true)
		{
			System.out.println("");
			System.out.println("Main Menu");
			System.out.println();
			System.out.println("Options");
			System.out.println("1.Payroll Report");
			System.out.println("2.Average Wage by Gender");
			System.out.println("3.Deductions Report");
			System.out.println("4.Exit");
			System.out.println("Please Choose an option:");
			
			opt=key.nextInt();
			
			if(opt==1)
			{
				printReport();
			}
			else if(opt==2)
			{
				printAvgWage();
			}
			else if(opt==3)
			{
				printTotalDeductions();
			}
			else
			{
				break;
			}
		}
		
	}
		
	public static void printReport() throws Exception
	{
		//Set up scanner and file
		File file=new File("Data/Peopledetails.txt");
		fs=new Scanner(file);
		fs.useDelimiter(",|\\r\n");
		
		//Print out headers for table
		System.out.printf("%5s%20s%10s%15s%20s%15s%15s%20s%10s%20s%10s\n\n", 
				"ID", "Name", "Gender", "Hourly Rate", "Contracted Hours",	"Gross Pay", 
				"Income Tax", "National Insurance", "Pension", "Total Deductions", "Net pay");
		
		while (fs.hasNext())
		{
			id=fs.nextInt();
			fname=fs.next();
			sname=fs.next();
			gen=fs.next();
			hrate=fs.nextDouble();
			hrs=fs.nextDouble();
						
			gross=hrs*hrate;
						
			//Calculates the overtime
			if(hrs>40)
			{
				otime=hrs-40;
				standard=40;
			}
			else
			{
				otime=0;
				standard=hrs;
			}
			
			gross=(standard*hrate+otime*hrate*1.5);
			
			//Calculates the annual gross in order to calculate the income tax per week
			annualgross=gross*52;
			
			//Calculates Income Tax
			double p,b,h,a;
			
			if(annualgross<=12500)
			{
				p=0;
				b=0;
				h=0;
				a=0;
			}
			else if(annualgross>12500 && annualgross<=50000)
			{
				p = 12500;
				b = annualgross - 12500;
				h = 0;
				a = 0;

			}
			else if(annualgross>50000 && annualgross<=150000)
			{
				p = 12500;
			    b = 37500;
			    h = annualgross - 50000;
			    a = 0;

			}
			else
			{
		        p = 12500;
		        b = 37500;
		        h = 100000;
		        a = annualgross - 150000;

			}
			tax= (p * 0 + b * 0.2 + h * 0.4 + a * 0.45)/52;
			
			//Calculates National Insurance Contribution
			if(annualgross>8500 && annualgross<50000)
			{
				nirate=0.12;
			}
			else
			{
				nirate=0.02;
			}
			nitax=gross*nirate;
			
			//Calculates Pension Contribution
			if(annualgross<27698)
			{
				penrate=0.074;
			}
			else if(annualgross>=27698 && annualgross<37285)
			{
				penrate=0.086;
			}
			else if(annualgross>=37285 && annualgross<44209)
			{
				penrate=0.096;
			}
			else
			{
				penrate=0.117;
			}
			pension=gross*penrate;
			
			//Calculates total deductions
			deduct=tax+nitax+pension;
			
			//Calculates net pay
			net=gross-deduct;
			
			
			System.out.printf("%5d%20s%10s%15.2f%20.2f%15.2f%15.2f%15.2f%15.2f%20.2f%10.2f\n",
					id, fname+" "+sname, gen, hrate, hrs, gross, tax, nitax, pension, deduct, net);
		}
		fs.close();
		
	}
	
	public static void printAvgWage() throws Exception
	{
		while(true)
		{
		System.out.print("Please enter a gender (m or f): ");
		gen=key.next();
		System.out.printf("Average wage for gender %s is: £%.2f\n",
		gen, getGenderAvgWage(gen));
		System.out.println();
		System.out.print("Would you like to see total wages for this gender? (y or n): ");
		String total = key.next();
		if(total.equals("y"))
		{
			System.out.printf("Total wage for gender %s is: £%.2f\n",
		gen, getGenderTotalWage(total));
		}
		else 
		{
			break;
		}
		}
	}
	//It will print average wages and total wages by gender
	public static double getGenderAvgWage(String g) throws Exception
	{
		File file=new File("Data/Peopledetails.txt");
		fs=new Scanner(file);
		fs.useDelimiter(",|\\r\n");
			
		totalWage=0;
		count=0;
		
						
		while (fs.hasNext())
		{
			id=fs.nextInt();
			fname=fs.next();
			sname=fs.next();
			gen=fs.next();
			hrate=fs.nextDouble();
			hrs=fs.nextDouble();
			
			gross=hrs*hrate;
			
			//Calculates the overtime
			if(hrs>40)
			{
				otime=hrs-40;
				standard=40;
			}
			else
			{
				otime=0;
				standard=hrs;
			}
			
			gross=(standard*hrate+otime*hrate*1.5);
			
			/*if the gender is male, it takes the gross pay of that person, adds it to the total for male wages
			and also adds 1 to the male counter.*/
			//otherwise, adds it to the total for female wages and 1 to female counter.
			if(gen.equals(g))
			{
				totalWage=gross+totalWage;
				count+=1;
			}
		}
		fs.close();
		return totalWage/count;
	}
	
	public static double getGenderTotalWage(String g) throws Exception
	{
		File file=new File("Data/Peopledetails.txt");
		fs=new Scanner(file);
		fs.useDelimiter(",|\\r\n");
			
		totalWage=0;
		
		
						
		while (fs.hasNext())
		{
			id=fs.nextInt();
			fname=fs.next();
			sname=fs.next();
			gen=fs.next();
			hrate=fs.nextDouble();
			hrs=fs.nextDouble();
			
			gross=hrs*hrate;
			
			//Calculates the overtime
			if(hrs>40)
			{
				otime=hrs-40;
				standard=40;
			}
			else
			{
				otime=0;
				standard=hrs;
			}
			
			gross=(standard*hrate+otime*hrate*1.5);
			
			
			/*if the gender is male, it takes the gross pay of that person, adds it to the total for male wages
			and also adds 1 to the male counter.*/
			//otherwise, adds it to the total for female wages and 1 to female counter.
			if(gen.equals(g))
			{
				totalWage=gross+totalWage;
			}
		}
		fs.close();
		return totalWage=totalWage+gross;
	}
	
	//Calculates all deductions for every individual and prints the sum
	public static void printTotalDeductions() throws Exception
	{
		File file=new File("Data/Peopledetails.txt");
		fs=new Scanner(file);
		fs.useDelimiter(",|\\r\n");
		
		//Print out headers for table
		System.out.printf("\n\n%10s%10s%15s\n\n","Total Tax","Total NI", "Total Pension");
		
		totaltax=0;
		totalni=0;
		totalpen=0;
		
		while (fs.hasNext())
		{
			id=fs.nextInt();
			fname=fs.next();
			sname=fs.next();
			gen=fs.next();
			hrate=fs.nextDouble();
			hrs=fs.nextDouble();
			
			gross=hrs*hrate;
			
			//Calculates the overtime
			if(hrs>40)
			{
				otime=hrs-40;
				standard=40;
			}
			else
			{
				otime=0;
				standard=hrs;
			}
			
			gross=(standard*hrate+otime*hrate*1.5);
			
			//Calculates the annual gross in order to calculate the income tax per week
			annualgross=gross*52;
			
			//Calculates Income Tax
			double p,b,h,a;
			
			if(annualgross<=12500)
			{
				p=0;
				b=0;
				h=0;
				a=0;
			}
			else if(annualgross>12500 && annualgross<=50000)
			{
				p = 12500;
				b = annualgross - 12500;
				h = 0;
				a = 0;

			}
			else if(annualgross>50000 && annualgross<=150000)
			{
				p = 12500;
			    b = 37500;
			    h = annualgross - 50000;
			    a = 0;

			}
			else
			{
		        p = 12500;
		        b = 37500;
		        h = 100000;
		        a = annualgross - 150000;

			}
			tax= (p * 0 + b * 0.2 + h * 0.4 + a * 0.45)/52;
			
			//Calculates National Insurance Contribution
			if(annualgross>8500 && annualgross<50000)
			{
				nirate=0.12;
			}
			else
			{
				nirate=0.02;
			}
			nitax=gross*nirate;
			
			//Calculates Pension Contribution
			if(annualgross<27698)
			{
				penrate=0.074;
			}
			else if(annualgross>=27698 && annualgross<37285)
			{
				penrate=0.086;
			}
			else if(annualgross>=37285 && annualgross<44209)
			{
				penrate=0.096;
			}
			else
			{
				penrate=0.117;
			}
			pension=gross*penrate;
		
			//Calculates all income tax
			totaltax=totaltax+tax;
			//Calculates all national insurance tax
			totalni=totalni+nitax;
			//Calculates all pension contributions
			totalpen=totalpen+pension;
		}
		System.out.printf("%10.2f%10.2f%15.2f\n", totaltax,totalni,totalpen);
		fs.close();
	}
}
