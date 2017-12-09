package br.com.caelum.estoque.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import br.com.caelum.estoque.modelo.item.Filtro;
import br.com.caelum.estoque.modelo.item.Filtros;
import br.com.caelum.estoque.modelo.item.Item;
import br.com.caelum.estoque.modelo.item.ItemDao;
import br.com.caelum.estoque.modelo.item.ItemValidador;
import br.com.caelum.estoque.modelo.item.ListaItens;
import br.com.caelum.estoque.modelo.usuario.AutorizacaoException;
import br.com.caelum.estoque.modelo.usuario.TokenDao;
import br.com.caelum.estoque.modelo.usuario.TokenUsuario;

@WebService
public class EstoqueWS {

	private ItemDao dao = new ItemDao();

	@WebMethod(exclude = true)
	public void outroMetodo() {
		// nao vai fazer parte do WSDL
	}

	@WebMethod(operationName = "TodosOsItens")
	@WebResult(name = "itens")
	public ListaItens getItens(@WebParam(name = "filtros") Filtros filtros) {
		System.out.println("Chamando todos os Items");
		List<Filtro> lista = filtros.getLista();
		List<Item> itensResultado = dao.todosItens(lista);

		return new ListaItens(itensResultado);
	}

	@WebMethod(operationName = "CadastrarItem")
	@WebResult(name = "item")
	public Item cadastrarItem(@WebParam(name = "tokenUsuario", header = true) TokenUsuario tokenUsuario,
			@WebParam(name = "item") Item item) throws AutorizacaoException {

		boolean tokenValido = new TokenDao().ehValido(tokenUsuario);

		if (tokenValido) {
			System.out.println("Cadastrando item: " + item);
			
			new ItemValidador(item).validate();
			this.dao.cadastrar(item);
		} else {
			throw new AutorizacaoException("falha ao cadastrar");
		}

		return item;
	}

	// @WebMethod(operationName = "todosOsItens")
	// @WebResult(name = "itens")
	// @ResponseWrapper(localName = "itens")
	// public ListaItens getItens(@WebParam(name = "filtros") Filtros filtros) {
	// System.out.println("Chamando todos os Items");
	// List<Filtro> lista = filtros.getLista();
	// List<Item> itensResultado = dao.todosItens(lista);
	//
	// return new ListaItens(itensResultado);
	// }

	// @WebMethod(operationName="todosOsItens")
	// @ResponseWrapper(localName="itens")
	// @RequestWrapper(localName="listaItens")
	// @WebResult(name="item")
	// public List<Item> getItens() {
	//
	// System.out.println("Chamando getItens()");
	// return dao.todosItens();
	//
	// }

}
