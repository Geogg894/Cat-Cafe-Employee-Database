import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;



public class CatTree implements Iterable<CatInfo>{
	public CatNode root;

	public CatTree(CatInfo c) {
		this.root = new CatNode(c);
	}

	private CatTree(CatNode c) {
		this.root = c;
	}


	public void addCat(CatInfo c)
	{
		this.root = root.addCat(new CatNode(c));

	}

	public void removeCat(CatInfo c)
	{
		this.root = root.removeCat(c);
	}

	public int mostSenior()
	{
		return root.mostSenior();
	}

	public int fluffiest() {
		return root.fluffiest();
	}

	public CatInfo fluffiestFromMonth(int month) {
		return root.fluffiestFromMonth(month);
	}

	public int hiredFromMonths(int monthMin, int monthMax) {
		return root.hiredFromMonths(monthMin, monthMax);
	}

	public int[] costPlanning(int nbMonths) {
		return root.costPlanning(nbMonths);
	}



	public Iterator<CatInfo> iterator()
	{
		return new CatTreeIterator();
	}


	class CatNode {

		CatInfo data;
		CatNode senior;
		CatNode same;
		CatNode junior;

		public CatNode(CatInfo data) {
			this.data = data;
			this.senior = null;
			this.same = null;
			this.junior = null;
		}

		public String toString() {
			String result = this.data.toString() + "\n";
			if (this.senior != null) {
				result += "more senior " + this.data.toString() + " :\n";
				result += this.senior.toString();
			}
			if (this.same != null) {
				result += "same seniority " + this.data.toString() + " :\n";
				result += this.same.toString();
			}
			if (this.junior != null) {
				result += "more junior " + this.data.toString() + " :\n";
				result += this.junior.toString();
			}
			return result;
		}



		public CatNode addCat(CatNode c) { 


			if(this.data.monthHired>c.data.monthHired) {


				if(this.senior==null) {

					this.senior=c;
				}

				else {

					this.senior.addCat(c);	
				}

			}

			if(this.data.monthHired==c.data.monthHired) {

				if(c.data.furThickness>this.data.furThickness) {

					CatInfo x=this.data;

					this.data=c.data;
					c.data=x;


					if(this.same==null) {

						this.same=c;
					}

					else {

						this.same.addCat(c);
					}

				}

				if(this.data.furThickness>c.data.furThickness) {

					if(this.same==null) {

						this.same=c;
					}

					else {

						this.same.addCat(c);
					}

				}

			}

			if(c.data.monthHired>this.data.monthHired) {

				if(this.junior==null) {

					this.junior=c;
				}

				else {

					this.junior.addCat(c);
				}
			}

			return this;
		}


		public CatNode removeCat(CatInfo c) { //good


			if(this.data.monthHired>c.monthHired) {

				if(this.senior==null) {

					return this;
				}

				else { 

					this.senior=this.senior.removeCat(c);

				}
			}


			if(c.monthHired>this.data.monthHired) {

				if(this.junior==null) {

					return this;
				}

				else {

					this.junior=this.junior.removeCat(c);

				}

			}

			else {

				if(this.data.furThickness>c.furThickness) {

					if(this.same==null) {

						return this;
					}

					else {

						this.same.removeCat(c);

					}

				}

				else {

					if(c==this.data){

						CatNode x=this.junior;
						CatNode y=this.senior;

						if(this.same!=null) {

							this.same.junior=x;
							this.same.senior=y;
							return this.same;
						}

						else {

							if(this.senior!=null) {

								if(this.junior!=null) {

									this.senior.addCat(this.junior);
								}

							}


						}
						return this.senior;
					}

					else {

						if(this.junior!=null) {

							return this.junior;

						}
					}
				}
			}


			return this; 
		}


		public int mostSenior() { //good


			if(this.senior==null) {

				return this.data.monthHired;
			}

			else {

				return this.senior.mostSenior();
			}

		}


		public int fluffiest() {  //good

			if(this.senior==null&&this.junior==null) {

				return this.data.furThickness;
			}

			else {

				int x=this.data.furThickness;

				if(this.senior!=null) {
					x=Math.max(x, this.senior.fluffiest());	
				}


				if(this.junior!=null) {
					x=Math.max(x, this.junior.fluffiest());		
				}

				return x;
			}


		}


		public int hiredFromMonths(int monthMin, int monthMax) { //good

			int counter=1;

			if(monthMin>monthMax||0>=monthMin&&0>=monthMax) { 

				return 0;
			}


			if(this.senior!=null) {

				if(monthMax>=this.senior.data.monthHired&&this.senior.data.monthHired>=monthMin) { 

					counter+=this.senior.hiredFromMonths(monthMin, monthMax);
				}
				else {

					this.senior.hiredFromMonths(monthMin, monthMax);
				}

			}

			if(this.junior!=null) {

				if(monthMax>=this.junior.data.monthHired&&this.junior.data.monthHired>=monthMin) { 

					counter+=this.junior.hiredFromMonths(monthMin, monthMax);
				}
				else {

					this.junior.hiredFromMonths(monthMin, monthMax);
				}
			}

			if(this.same!=null) {

				if(monthMax>=this.data.monthHired&&this.data.monthHired>=monthMin) {

					counter+=this.same.hiredFromMonths(monthMin, monthMax);
				}

				else {

					this.same.hiredFromMonths(monthMin, monthMax);
				}

			}

			return counter;
		}


		public CatInfo fluffiestFromMonth(int month) { //good

			if(this.senior==null&&this.junior==null) {

				if(this.data.monthHired==month) {

					return this.data;
				}
				else {

					return null;
				}

			}

			else {


				if(month>this.data.monthHired) {

					if(this.junior!=null) {

						this.junior.fluffiestFromMonth(month);

						if(this.data.monthHired==month) {

							return this.data;
						}

					}

					else {

						return null;
					}

				}


				if(this.data.monthHired>month) {

					if(this.senior!=null) {

						this.senior.fluffiestFromMonth(month);

						if(this.data.monthHired==month) {

							return this.data;
						}

					}

					else {

						return null;

					}

				}

				if(this.data.monthHired==month) {

					return this.data;
				}


			}
			return this.data;		
		}


		public int[] costPlanning(int nbMonths) {

			int[] array = new int[nbMonths];
			CatTreeIterator iterator = new CatTreeIterator(); 
			int i=0;
			CatTree Tree;

			if(this!=root) {

				Tree=new CatTree(this);
			}

			else {

				Tree=CatTree.this;
			}


			for(CatInfo data:Tree) {

				if(data.nextGroomingAppointment>=243&&243+nbMonths>=data.nextGroomingAppointment) {

					if(data.nextGroomingAppointment==243+i) {

						array[i]+=data.expectedGroomingCost;

					}
					
					else {
						
						data= iterator.next();
					}

				}
				i++;
			}

			return array;
		}

	}

	private class CatTreeIterator implements Iterator<CatInfo> {

		int counter=0;
		ArrayList<CatInfo> list=new ArrayList<CatInfo>();


		public void inordertraversalIterator(CatNode root){

			if (root == null) return;

			else {

				inordertraversalIterator(root.senior);
				inordertraversalIterator(root.same);
				list.add(root.data);
				inordertraversalIterator(root.junior);

			}

		}

		public CatTreeIterator() { 

			inordertraversalIterator(root);
		}


		public boolean hasNext() {     

			return list.size()>counter;
		}


		public CatInfo next(){ 


			if(!hasNext()) {

				throw new NoSuchElementException("No element was found.");
			}

			else {

				return (CatInfo) list.get(counter++);

			}
		}


	}

}

