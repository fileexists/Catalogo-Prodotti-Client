package dev.deyvid.catalogoprodotticlient.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import dev.deyvid.catalogoprodotticlient.client.CatalogoClient;
import dev.deyvid.catalogoprodotticlient.model.Prodotto;
import dev.deyvid.catalogoprodotticlient.model.Utente;
import jakarta.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes("utenteSessione")
public class ProdottoController {
	/*

		--dovrà esporre i metodi che consentano all’utente di navigare verso le 
			pagine creaProdotto.jsp e ricercaProdotti.jsp
		
		--dovrà esporre un metodo per creare un prodotto sul db, sfruttando i Service Spring, 
			e inoltri la response verso la pagina esito.jsp con un opportuno messaggio per differenziare i casi in cui 
			l’inserimento è avvenuto con successo rispetto ai casi in cui si è verificato un errore
			
		--dovrà esporre un metodo per ricercare i prodotti sul db, sfruttando i Service Spring e:
			in caso di errori inoltri la response verso la pagina esito.jsp con un opportuno messaggio
			altrimenti inoltri la response vero la pagina risultatiRicercaProdotti.jsp a cui passerà la List contenente i risultati della ricerca

	 */
	
	@Autowired
	private CatalogoClient catalogoClient;
	
	@GetMapping("/prodotti")
	public String prodotti(Model model) {
		try {
			return "index";
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			model.addAttribute("errore", "errore imprevisto");
			return "errore";
		}
	}
	
	@GetMapping("/ricercaProdotti")
	public String goToRicercaProdotti() {
		return "ricercaProdotti";
	}
	
	@GetMapping("/creaProdotto")
	public String goToCreaProdotto() {
		return "creaProdotto";
	}
	
	@ModelAttribute("prodotto")
	public Prodotto initEmptyProdotto() {
		return new Prodotto();
	}
	
    @ModelAttribute("utenteSessione")
    public Utente getUtenteSessione(@ModelAttribute("utenteSessione") Utente utente) {
        return utente;
    }
	
	@PostMapping("/creaProdotto")
	public String creaProdotto(
		Model model, 
		@ModelAttribute("prodotto") Prodotto parametroProdotto,
	    @ModelAttribute("utenteSessione") Utente utente
	) {
		try {
			ResponseEntity<Void> prodotto = catalogoClient.creaProdotto(utente, parametroProdotto);
			String message = "";
			if(prodotto.getStatusCode() == HttpStatus.CREATED) {
				message = "Prodotto creato con successo.";
			}
			else {
				message = "Prodotto già esistente...";
			}
			return "redirect:/esito?message="+message;
		}
		catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errore", "errore imprevisto");
			return "errore";
		}
	}
	
	@GetMapping("/esito")
	public String esito(Model model, HttpServletRequest request) {
		String messaggio = request.getParameter("message");
		if(messaggio != null && !messaggio.isEmpty()) {
			model.addAttribute("message", messaggio);
		}
		return "esito";
	}
	
	@PostMapping("/ricercaProdotti")
    public String ricercaProdotti(
		@ModelAttribute("prodotto") Prodotto parametroProdotto,
	    @ModelAttribute("utenteSessione") Utente utente,
		Model model, 
		HttpServletRequest request
	) {
        try {
            String nome = parametroProdotto.getNome();
            Float prezzo = parametroProdotto.getPrezzo();

            ResponseEntity<List<Prodotto>> response = catalogoClient.ricercaProdotti(utente, nome, prezzo);
            if(response.getStatusCode() == HttpStatus.OK ) {
            	List<Prodotto> prodotti = response.getBody();
            	if (prodotti.isEmpty()) {
                    model.addAttribute("message", "Nessun prodotto trovato");
                    return "esito";
                }
                request.setAttribute("prodotti", prodotti);
                return "forward:/risultatiRicercaProdotti";
            }
            else if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                model.addAttribute("message", "Nessun prodotto trovato");
                return "esito";
            }
            
        	model.addAttribute("message", "Assicurati di compilare bene il form di ricerca");
            return "esito";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "ERRORE!!! Ricerca dei prodotti fallita");
            return "esito";
        }
    }

    @PostMapping("/risultatiRicercaProdotti")
    public String risultatiRicercaProdotti(Model model, HttpServletRequest request) {
    	try {
        	List<Prodotto> prodotti = (List<Prodotto>) request.getAttribute("prodotti");
            if (prodotti != null && prodotti.isEmpty()) {
                model.addAttribute("prodotti", prodotti);
            } else {
                model.addAttribute("message", "Nessun prodotto trovato");
            }

            return "risultatiRicercaProdotti";
    	} catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "ERRORE!!! Ricerca dei prodotti fallita");
            return "esito";
        }

    }
	
    @PostMapping("/totalUpdate")
    public String totalUpdate(
    	Model model,
		HttpServletRequest request,
	    @ModelAttribute("utenteSessione") Utente utente
	) {
    	try {
	        Integer id = Integer.parseInt(request.getParameter("id"));
	        String nome = request.getParameter("nome");
	        Float prezzo = Float.parseFloat(request.getParameter("prezzo"));
	
	        ResponseEntity<Void> aggiornamento = catalogoClient.totalUpdate(utente, id, nome, prezzo);
			if(aggiornamento.getStatusCode() == HttpStatus.CREATED) {
				return "redirect:/homeInterna";
			}
			else if (aggiornamento.getStatusCode() == HttpStatus.NOT_FOUND) {
				return "redirect:/esito?message=Prodotto non trovato";
			}
			
			return "redirect:/esito?message=Errore durante l aggiornamento..";
    	}catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "ERRORE!!! Ricerca dei prodotti fallita");
            return "esito";
        }
    }

    @PostMapping("/partialUpdate")
    public String partialUpdate(	
    	Model model,
		HttpServletRequest request,
		@ModelAttribute("utenteSessione") Utente utente
	) {
    	try {
	        Integer id = Integer.parseInt(request.getParameter("id"));
	        String nome = request.getParameter("nome");
	        Float prezzo = null;
	        if (request.getParameter("prezzo") != null) {
	        	prezzo = Float.parseFloat(request.getParameter("prezzo"));
	        }
	        ResponseEntity<Void> aggiornamento = catalogoClient.partialUpdate(utente, id, nome, prezzo);
			if(aggiornamento.getStatusCode() == HttpStatus.CREATED) {
				return "redirect:/homeInterna";
			}
			else if (aggiornamento.getStatusCode() == HttpStatus.NOT_FOUND) {
				return "redirect:/esito?message=Prodotto non trovato";
			}
			
			return "redirect:/esito?message=Errore duarante l aggiornamento..";
    	}catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "ERRORE!!! Errore sconosciuto.");
            return "esito";
        }
    }

    @PostMapping("/delete")
    public String delete(HttpServletRequest request, @ModelAttribute("utenteSessione") Utente utente) {
        Integer id = Integer.parseInt(request.getParameter("id"));
        ResponseEntity<Void> cancellazione = catalogoClient.delete(utente, id);

		if(cancellazione.getStatusCode() == HttpStatus.NO_CONTENT) {
			return "redirect:/esito?message=Cancellazione avvenuta con successo";
		}
		return "redirect:/esito?message=Errore durante la cancellazione..";
    }

}
